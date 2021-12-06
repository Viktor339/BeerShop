package com.shop.service;

import com.shop.config.Config;
import com.shop.model.BottleBeerData;
import com.shop.repository.PositionRepository;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.service.validator.ContainerTypeValidator;
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
                new NotEmptyFieldValidator<>(ChangePositionRequest::getName, "Name is null or empty"),
                new NotEmptyFieldValidator<>(ChangePositionRequest::getContainerType, "Container type is null or empty"),
                new NotEmptyFieldValidator<>(ChangePositionRequest::getNewContainerVolume, "New container volume is null or empty"),
                new NotEmptyFieldValidator<>(ChangePositionRequest::getNewQuantity, "New quantity is null or empty"),
                new ContainerVolumeValidator<>(ChangePositionRequest::getNewContainerVolume, config.getMinContainerVolume(), config.getMaxContainerVolume(), "Incorrect container volume"),
                new ContainerTypeValidator<>(ChangePositionRequest::getContainerType, "Incorrect container type")
        );
    }


    public ChangePositionResponse change(ChangePositionRequest changePositionRequest) {

        String name = changePositionRequest.getName();
        String containerType = changePositionRequest.getContainerType();
        Double newContainerVolume = changePositionRequest.getNewContainerVolume();
        Integer newQuantity = changePositionRequest.getNewQuantity();

        validatorService.validate(changePositionRequestValidator, changePositionRequest);

        if (!positionRepository.existsPositionByNameOrContainerType(name, containerType)) {
            throw new PositionNotFoundException("Position not found");
        }

        ChangePositionResponse changePositionResponse = ChangePositionResponse.builder()
                .name(name)
                .containerType(containerType)
                .beerInfo(new BottleBeerData(newContainerVolume, newQuantity))
                .build();

        return positionRepository.update(changePositionResponse);
    }
}
