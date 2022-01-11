package com.shop.service;

import com.shop.model.BottleBeerData;
import com.shop.repository.PositionRepository;
import com.shop.repository.TransactionalHandler;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.ChangePositionResponse;
import com.shop.servlet.request.ChangePositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class ChangePositionService {
    private final PositionRepository positionRepository;
    private final List<Validator<ChangePositionRequest>> changePositionRequestValidator;
    private final ValidatorService validatorService;
    private final TransactionalHandler transactionalHandler;

    public ChangePositionService(PositionRepository positionRepository,
                                 ValidatorService validatorService,
                                 List<Validator<ChangePositionRequest>> changePositionRequestValidator,
                                 TransactionalHandler transactionalHandler) {
        this.validatorService = validatorService;
        this.positionRepository = positionRepository;
        this.changePositionRequestValidator = changePositionRequestValidator;
        this.transactionalHandler = transactionalHandler;

    }


    public ChangePositionResponse change(ChangePositionRequest changePositionRequest) {

        Integer id = changePositionRequest.getId();
        Double containerVolume = changePositionRequest.getContainerVolume();
        Integer quantity = changePositionRequest.getQuantity();

        validatorService.validate(changePositionRequestValidator, changePositionRequest);

        BottleBeerData beerInfo = new BottleBeerData(containerVolume, quantity);

        transactionalHandler.doTransaction(session -> {

            if (!positionRepository.existsPositionById(id, session)) {
                throw new PositionNotFoundException("Position not found");
            }

            positionRepository.update(beerInfo, id, session);
        });


        return ChangePositionResponse.builder()
                .id(id)
                .beerInfo(beerInfo)
                .build();
    }
}
