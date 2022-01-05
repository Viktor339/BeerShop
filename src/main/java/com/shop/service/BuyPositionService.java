package com.shop.service;

import com.shop.repository.PositionRepository;
import com.shop.repository.UserRepository;
import com.shop.repository.UserTransactionRepository;
import com.shop.service.exception.ValidatorException;
import com.shop.service.performer.Performer;
import com.shop.servlet.dto.BuyPositionDto;
import com.shop.servlet.request.BuyPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class BuyPositionService {
    private final PositionRepository positionRepository;
    private final UserTransactionRepository userTransactionRepository;
    private final List<Performer<BuyPositionRequest, List<BuyPositionDto>>> buyBeerPerformer;
    private final UserRepository userRepository;

    private final List<Performer<BuyPositionRequest, String>> buyBeerDataValidatorPerformer;


    public BuyPositionService(List<Performer<BuyPositionRequest, List<BuyPositionDto>>> buyBeerPerformer,
                              PositionRepository positionRepository,
                              UserTransactionRepository userTransactionRepository,
                              UserRepository userRepository,
                              List<Performer<BuyPositionRequest, String>> buyBeerDataValidatorPerformer) {
        this.buyBeerPerformer = buyBeerPerformer;
        this.positionRepository = positionRepository;
        this.userTransactionRepository = userTransactionRepository;
        this.userRepository = userRepository;
        this.buyBeerDataValidatorPerformer = buyBeerDataValidatorPerformer;

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
                    positionRepository.updatePositionAfterPurchase(dto.getPositionDto());
                    dto.setUserId(id);
                    userTransactionRepository.save(dto);
                });
    }
}
