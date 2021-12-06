package com.shop.service;

import com.shop.model.BuyBottleBeerData;
import com.shop.model.BuyDraftBeerData;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.repository.TransactionRepository;
import com.shop.service.performer.BuyBottleBeerPerformer;
import com.shop.service.performer.BuyDraftBeerPerformer;
import com.shop.service.performer.Performer;
import com.shop.service.validator.NotEmptyFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.BuyPositionTransactionDTO;
import com.shop.servlet.request.BuyPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BuyPositionService {
    private final PositionRepository positionRepository;
    private final TransactionRepository transactionRepository;
    private final List<Performer<BuyPositionRequest, Map<BuyPositionTransactionDTO, Position>>> buyBottleBeerPerformer;
    private final List<Validator<BuyBottleBeerData>> bottleBeerDataValidator;
    private final List<Validator<BuyDraftBeerData>> draftBeerDataValidator;


    public BuyPositionService(PositionRepository positionRepository, ValidatorService validatorService, TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.positionRepository = positionRepository;
        bottleBeerDataValidator = Arrays.asList(
                new NotEmptyFieldValidator<>(BuyBottleBeerData::getName, "Name is null or empty"),
                new NotEmptyFieldValidator<>(BuyBottleBeerData::getQuantity, "Quantity is null or empty")
        );
        draftBeerDataValidator = Arrays.asList(
                new NotEmptyFieldValidator<>(BuyDraftBeerData::getName, "Name is null or empty"),
                new NotEmptyFieldValidator<>(BuyDraftBeerData::getQuantity, "Quantity is null or empty")

        );
        buyBottleBeerPerformer = Arrays.asList(
                new BuyBottleBeerPerformer(validatorService, positionRepository, bottleBeerDataValidator),
                new BuyDraftBeerPerformer(validatorService, positionRepository, draftBeerDataValidator)
        );
    }

    public String buy(BuyPositionRequest buyPositionRequest, Object uuid) {

        List<Map<BuyPositionTransactionDTO, Position>> positionList = new ArrayList<>();
        buyBottleBeerPerformer.stream()
                .filter(n -> n.isValid(buyPositionRequest.getBuyPositionData()))
                .forEach(n -> positionList.add(n.perform(buyPositionRequest)));


        for (Map<BuyPositionTransactionDTO, Position> transactionByPosition : positionList) {
            for (Map.Entry<BuyPositionTransactionDTO, Position> position : transactionByPosition.entrySet()) {
                positionRepository.updatePositionAfterPurchase(position.getValue());

                BuyPositionTransactionDTO buyPositionTransactionDTO = position.getKey();
                buyPositionTransactionDTO.setUuid(uuid);

                transactionRepository.save(buyPositionTransactionDTO);
            }
        }

        return "Items purchased";
    }
}
