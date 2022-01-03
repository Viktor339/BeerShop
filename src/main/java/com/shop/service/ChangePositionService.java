package com.shop.service;

import com.shop.model.BeerInfo;
import com.shop.model.BottleBeerData;
import com.shop.repository.PositionRepository;
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

    public ChangePositionService(PositionRepository positionRepository,
                                 ValidatorService validatorService,
                                 List<Validator<ChangePositionRequest>> changePositionRequestValidator) {
        this.validatorService = validatorService;
        this.positionRepository = positionRepository;
        this.changePositionRequestValidator = changePositionRequestValidator;

    }


    public ChangePositionResponse change(ChangePositionRequest changePositionRequest) {

        Long id = changePositionRequest.getId();
        Double containerVolume = changePositionRequest.getContainerVolume();
        Integer quantity = changePositionRequest.getQuantity();

        validatorService.validate(changePositionRequestValidator, changePositionRequest);

        if (!positionRepository.existsPositionById(id)) {
            throw new PositionNotFoundException("Position not found");
        }

        BeerInfo beerInfo = new BottleBeerData(containerVolume, quantity);
        positionRepository.update(beerInfo, id);

        return ChangePositionResponse.builder()
                .id(id)
                .beerInfo(beerInfo)
                .build();
    }
}
