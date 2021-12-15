package com.shop.service;

import com.shop.model.BuyBottleBeerData;
import com.shop.model.BuyDraftBeerData;
import com.shop.repository.PositionRepository;
import com.shop.repository.UserRepository;
import com.shop.repository.UserTransactionRepository;
import com.shop.service.exception.ValidatorException;
import com.shop.service.performer.BuyBottleBeerDataValidatorPerformer;
import com.shop.service.performer.BuyBottleBeerPerformer;
import com.shop.service.performer.BuyDraftBeerDataValidatorPerformer;
import com.shop.service.performer.BuyDraftBeerPerformer;
import com.shop.service.performer.Performer;
import com.shop.service.validator.NotEmptyFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.BuyPositionDto;
import com.shop.servlet.request.BuyPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class BuyPositionService {
    private final PositionRepository positionRepository;
    private final UserTransactionRepository userTransactionRepository;
    private final List<Performer<BuyPositionRequest, List<BuyPositionDto>>> buyBeerPerformer;
    private final UserRepository userRepository;

    private final List<Validator<BuyDraftBeerData>> buyDraftBeerDataValidator;
    private final List<Validator<BuyBottleBeerData>> buyBottleBeerDataValidator;
    private final List<Performer<BuyPositionRequest, String>> buyBeerDataValidatorPerformer;


    public BuyPositionService(PositionRepository positionRepository, UserTransactionRepository userTransactionRepository, UserRepository userRepository) {
        this.userTransactionRepository = userTransactionRepository;
        this.positionRepository = positionRepository;
        this.userRepository = userRepository;

        buyBeerPerformer = Arrays.asList(
                new BuyBottleBeerPerformer(positionRepository),
                new BuyDraftBeerPerformer(positionRepository)
        );

        buyDraftBeerDataValidator = Arrays.asList(
                new NotEmptyFieldValidator<>(BuyDraftBeerData::getId, "Id is null or empty"),
                new NotEmptyFieldValidator<>(BuyDraftBeerData::getQuantity, "Quantity is null or empty")
        );

        buyBottleBeerDataValidator = Arrays.asList(
                new NotEmptyFieldValidator<>(BuyBottleBeerData::getId, "Id is null or empty"),
                new NotEmptyFieldValidator<>(BuyBottleBeerData::getQuantity, "Quantity is null or empty")
        );

        buyBeerDataValidatorPerformer = Arrays.asList(
                new BuyDraftBeerDataValidatorPerformer(buyDraftBeerDataValidator),
                new BuyBottleBeerDataValidatorPerformer(buyBottleBeerDataValidator)
        );
    }

    public void buy(BuyPositionRequest buyPositionRequest, Object uuid) {

        String message = buyBeerDataValidatorPerformer.stream()
                .filter(n -> n.isValid(buyPositionRequest))
                .map(n -> n.perform(buyPositionRequest))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
        if (message != null) {
            throw new ValidatorException(message);
        }

        Integer id = userRepository.getUserIdByUUID(uuid);

        buyBeerPerformer.stream()
                .filter(n -> n.isValid(buyPositionRequest))
                .map(n -> n.perform(buyPositionRequest))
                .flatMap(Collection::stream)
                .forEach(dto -> {
                    positionRepository.updatePositionAfterPurchase(dto.getPosition());
                    dto.setUserId(id);
                    userTransactionRepository.save(dto);
                });
    }
}
