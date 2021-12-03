package com.shop.service;

import com.shop.config.Config;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.service.validator.ContainerTypeValidator;
import com.shop.service.validator.ContainerVolumeValidator;
import com.shop.service.validator.NotNullFieldValidator;
import com.shop.service.validator.Validator;
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
                new NotNullFieldValidator<>(ChangePositionRequest::getName, "Name is null or empty"),
                new NotNullFieldValidator<>(ChangePositionRequest::getContainerType, "Container type is null or empty"),
                new NotNullFieldValidator<>(ChangePositionRequest::getNewContainerVolume, "New container volume is null or empty"),
                new NotNullFieldValidator<>(ChangePositionRequest::getNewQuantity, "New quantity is null or empty"),
                new ContainerVolumeValidator<>(ChangePositionRequest::getNewContainerVolume, config.getMinContainerVolume(), config.getMaxContainerVolume(), "Incorrect container volume"),
                new ContainerTypeValidator<>(ChangePositionRequest::getContainerType, "Incorrect container type")
        );
    }


    public Position change(ChangePositionRequest changePositionRequest) {

        String name = changePositionRequest.getName();
        String containerType = changePositionRequest.getContainerType();
        Double newContainerVolume = changePositionRequest.getNewContainerVolume();
        Integer newQuantity = changePositionRequest.getNewQuantity();

        validatorService.validate(changePositionRequestValidator, changePositionRequest);

        if (!positionRepository.getPositionByNameOrContainerType(name, containerType)) {
            throw new PositionNotFoundException("Position not found");
        }

        return positionRepository.update(newContainerVolume, newQuantity, name, containerType);
    }
}
