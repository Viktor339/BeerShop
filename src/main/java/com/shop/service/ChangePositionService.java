package com.shop.service;

import com.shop.config.Config;
import com.shop.model.BeerInfo;
import com.shop.model.BottleBeerData;
import com.shop.repository.PositionRepository;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.service.validator.ContainerVolumeValidator;
import com.shop.service.validator.NotEmptyFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.ChangePositionResponse;
import com.shop.servlet.request.ChangePositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;


@RequiredArgsConstructor
public class ChangePositionService {
    private final PositionRepository positionRepository;
    private final List<Validator<ChangePositionRequest>> changePositionRequestValidator;
    private final ValidatorService validatorService;

    public ChangePositionService(PositionRepository positionRepository, Config config, ValidatorService validatorService) {
        this.validatorService = validatorService;
        this.positionRepository = positionRepository;

        changePositionRequestValidator = Arrays.asList(
                new NotEmptyFieldValidator<>(ChangePositionRequest::getId, "Id is null or empty"),
                new NotEmptyFieldValidator<>(ChangePositionRequest::getContainerVolume, "Container volume is null or empty"),
                new NotEmptyFieldValidator<>(ChangePositionRequest::getQuantity, "Quantity is null or empty"),
                new ContainerVolumeValidator<>(ChangePositionRequest::getContainerVolume, config.getMinContainerVolume(), config.getMaxContainerVolume(), "Incorrect container volume")
        );
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
