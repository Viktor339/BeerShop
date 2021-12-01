package com.shop.service;

import com.shop.config.Config;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.service.validator.ContainerTypeValidator;
import com.shop.service.validator.ContainerVolumeValidator;
import com.shop.service.validator.NotEmptyFieldValidator;
import com.shop.service.validator.NotNullFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.ChangePositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;


@RequiredArgsConstructor
public class ChangePositionService {
    private final PositionRepository positionRepository;
    private final List<Validator<ChangePositionRequest>> changePositionRequestValidator;
    private final ValidatorService validatorService;
    private final Config config;

    public ChangePositionService(PositionRepository positionRepository) {
        config = new Config();
        validatorService = new ValidatorService();
        this.positionRepository = positionRepository;

        changePositionRequestValidator = Arrays.asList(
                new NotNullFieldValidator<>(ChangePositionRequest::getName, "Name is null"),
                new NotNullFieldValidator<>(ChangePositionRequest::getContainerType, "Container type is null"),
                new NotNullFieldValidator<>(ChangePositionRequest::getNewContainerVolume, "New container volume is null"),
                new NotNullFieldValidator<>(ChangePositionRequest::getNewQuantity, "New quantity is null"),
                new NotEmptyFieldValidator<>(ChangePositionRequest::getName, "Name is empty"),
                new NotEmptyFieldValidator<>(ChangePositionRequest::getNewContainerVolume, "New container volume is empty"),
                new NotEmptyFieldValidator<>(ChangePositionRequest::getNewQuantity, "New quantity is empty"),
                new NotEmptyFieldValidator<>(ChangePositionRequest::getContainerType, "Container type is empty"),
                new ContainerVolumeValidator<>(req -> parseInt(req.getNewContainerVolume()) >= parseInt(config.getMinContainerVolume()) &
                                parseInt(req.getNewContainerVolume()) <= parseInt(config.getMaxContainerVolume()), "Incorrect container volume"),
                new ContainerTypeValidator<>(req->req.getContainerType().equals("bottle"),"Incorrect container type")
        );
    }


    public Position change(ChangePositionRequest changePositionRequest) {

        String name = changePositionRequest.getName();
        String containerType = changePositionRequest.getContainerType();
        String newContainerVolume = changePositionRequest.getNewContainerVolume();
        String newQuantity = changePositionRequest.getNewQuantity();

        validatorService.validate(changePositionRequestValidator, changePositionRequest);

        if(!positionRepository.getPositionByNameOrContainerType(name,containerType)){
            throw new PositionNotFoundException("Position not found");
        }

        return positionRepository.update(newContainerVolume,newQuantity,name,containerType);
    }
}
