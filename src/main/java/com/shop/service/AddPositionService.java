package com.shop.service;

import com.shop.config.Config;
import com.shop.model.BottleBeerData;
import com.shop.model.DraftBeerData;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.service.exception.PositionAlreadyExistsException;
import com.shop.service.validator.AlcoholPercentageValidator;
import com.shop.service.validator.BitternessValidator;
import com.shop.service.validator.ContainerTypeValidator;
import com.shop.service.validator.ContainerVolumeValidator;
import com.shop.service.validator.NotEmptyFieldValidator;
import com.shop.service.validator.NotNullFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.AddPositionResponse;
import com.shop.servlet.request.AddPositionRequest;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.*;

@RequiredArgsConstructor
public class AddPositionService {
    private final PositionRepository positionRepository;
    private final List<Validator<AddPositionRequest>> positionRequestValidator;
    private final List<Validator<DraftBeerData>> draftBeerValidator;
    private final List<Validator<BottleBeerData>> bottleBeerValidator;
    private final ValidatorService validatorService;
    private final Config config;

    public AddPositionService(PositionRepository positionRepository) {
        config = new Config();
        validatorService = new ValidatorService();
        this.positionRepository = positionRepository;

        positionRequestValidator = Arrays.asList(
                new NotNullFieldValidator<>(AddPositionRequest::getName, "Name is null"),
                new NotNullFieldValidator<>(AddPositionRequest::getContainerType, "Container type is null"),
                new NotNullFieldValidator<>(AddPositionRequest::getBeerType, "Beer type is null"),
                new NotNullFieldValidator<>(AddPositionRequest::getAlcoholPercentage, "Alcohol percentage is null"),
                new NotNullFieldValidator<>(AddPositionRequest::getBitterness, "Bitterness is null"),
                new NotEmptyFieldValidator<>(AddPositionRequest::getName, "Name is empty"),
                new NotEmptyFieldValidator<>(AddPositionRequest::getContainerType, "Container type is empty"),
                new NotEmptyFieldValidator<>(AddPositionRequest::getBeerType, "Beer type is empty"),
                new NotEmptyFieldValidator<>(AddPositionRequest::getAlcoholPercentage, "Alcohol percentage is empty"),
                new NotEmptyFieldValidator<>(AddPositionRequest::getBitterness, "Bitterness is empty"),
                new ContainerTypeValidator<>(req -> req.getContainerType().equals("bottle") | req.getContainerType().equals("draft"), "Incorrect container type"),
                new AlcoholPercentageValidator(config.getMinAlcoholPercentage(), config.getMaxAlcoholPercentage(), "Incorrect alcohol percentage"),
                new BitternessValidator(config.getMinBitterness(), config.getMAxBitterness(), "Incorrect bitterness value")
        );

        draftBeerValidator = Arrays.asList(
                new NotNullFieldValidator<>(DraftBeerData::getAvailableLiters, "Available litres is null"),
                new NotEmptyFieldValidator<>(DraftBeerData::getAvailableLiters, "Available litres is empty")
        );

        bottleBeerValidator = Arrays.asList(
                new NotNullFieldValidator<>(BottleBeerData::getContainerVolume, "Container volume is null"),
                new NotNullFieldValidator<>(BottleBeerData::getQuantity, "Quantity is null"),
                new NotEmptyFieldValidator<>(BottleBeerData::getContainerVolume, "Container volume is empty"),
                new NotEmptyFieldValidator<>(BottleBeerData::getQuantity, "Quantity is empty"),
                new ContainerVolumeValidator<>(data -> parseInt(data.getContainerVolume()) > parseInt(config.getMinContainerVolume()) |
                        parseInt(data.getContainerVolume()) <= parseInt(config.getMaxContainerVolume()), "Incorrect container volume")
                );
    }

    public AddPositionResponse add(AddPositionRequest addPositionRequest) {

        String name = addPositionRequest.getName();
        String beerType = addPositionRequest.getBeerType();
        int alcoholPercentage = addPositionRequest.getAlcoholPercentage();
        String bitterness = addPositionRequest.getBitterness();
        String containerType = addPositionRequest.getContainerType();

        validatorService.validate(positionRequestValidator, addPositionRequest);

        if (positionRepository.getPositionByNameOrContainerType(name, containerType)) {
            throw new PositionAlreadyExistsException("Position already exists");
        }

        AddPositionResponse addPositionResponse;
        Position position;

        if (addPositionRequest.getContainerType().equals("draft")) {
            DraftBeerData draftBeerData = (DraftBeerData) addPositionRequest.getBeerInfo();
            validatorService.validate(draftBeerValidator, draftBeerData);

            position = positionRepository.savePosition(name,
                    beerType,
                    alcoholPercentage,
                    bitterness,
                    containerType,
                    draftBeerData.getAvailableLiters(),
                    new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis()));

            addPositionResponse = new AddPositionResponse(position.getId(),
                    position.getName(),
                    position.getBeerType(),
                    position.getAlcoholPercentage(),
                    position.getBitterness(),
                    position.getContainerType(),
                    new DraftBeerData(position.getAvailableLitres()));
        } else {
            BottleBeerData bottleBeerData = (BottleBeerData) addPositionRequest.getBeerInfo();
            validatorService.validate(bottleBeerValidator, bottleBeerData);

            position = positionRepository.savePosition(name,
                    beerType,
                    alcoholPercentage,
                    bitterness,
                    containerType,
                    bottleBeerData.getContainerVolume(),
                    bottleBeerData.getQuantity(),
                    new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis()));

            addPositionResponse = new AddPositionResponse(position.getId(),
                    position.getName(),
                    position.getBeerType(),
                    position.getAlcoholPercentage(),
                    position.getBitterness(),
                    position.getContainerType(),
                    new BottleBeerData(position.getContainerVolume(), position.getQuantity()));
        }
        return addPositionResponse;
    }
}
