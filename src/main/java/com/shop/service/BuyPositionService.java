package com.shop.service;

import com.shop.model.BuyBeerData;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.repository.UserRepository;
import com.shop.repository.UserTransactionRepository;
import com.shop.service.performer.BuyBottleBeerPerformer;
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
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class BuyPositionService {
    private final PositionRepository positionRepository;
    private final UserTransactionRepository userTransactionRepository;
    private final List<Performer<BuyPositionRequest, Map<Position, Double>>> buyBottleBeerPerformer;
    private final ValidatorService validatorService;
    private final UserRepository userRepository;

    private final List<Validator<BuyBeerData>> buyBeerDataValidator;


    public BuyPositionService(PositionRepository positionRepository, ValidatorService validatorService, UserTransactionRepository userTransactionRepository, UserRepository userRepository) {
        this.userTransactionRepository = userTransactionRepository;
        this.positionRepository = positionRepository;
        this.validatorService = validatorService;
        this.userRepository = userRepository;

        buyBottleBeerPerformer = Arrays.asList(
                new BuyBottleBeerPerformer(positionRepository),
                new BuyDraftBeerPerformer(positionRepository)
        );

        buyBeerDataValidator = Arrays.asList(
                new NotEmptyFieldValidator<>(BuyBeerData::getId, "Id is null or empty"),
                new NotEmptyFieldValidator<>(BuyBeerData::getQuantity, "Quantity is null or empty")
        );
    }

    public void buy(BuyPositionRequest buyPositionRequest, Object uuid) {

        Stream.of(buyPositionRequest.getDraft(), buyPositionRequest.getBottle())
                .flatMap(Collection::stream)
                .forEach(beer -> validatorService.validate(buyBeerDataValidator, beer));

        Integer id = userRepository.getUserIdByUUID(uuid);

        buyBottleBeerPerformer.stream()
                .filter(n -> n.isValid(buyPositionRequest))
                .map(n -> n.perform(buyPositionRequest))
                .flatMap(positions -> positions.entrySet().stream())
                .forEach(position -> {
                    positionRepository.updatePositionAfterPurchase(position.getKey());

                    BuyPositionDto buyPositionDto = BuyPositionDto.builder()
                            .userId(id)
                            .name(position.getKey().getName())
                            .quantity(position.getValue())
                            .build();

                    userTransactionRepository.save(buyPositionDto);
                });
    }
}
