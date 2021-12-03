package com.shop.service;

import com.shop.config.Config;
import com.shop.model.BottleBeerData;
import com.shop.model.DraftBeerData;
import com.shop.repository.PositionRepository;
import com.shop.service.exception.BeerPositionExecutorNotFoundException;
import com.shop.service.exception.PositionAlreadyExistsException;
import com.shop.service.executor.BottleBeerExecutor;
import com.shop.service.executor.DraftBeerExecutor;
import com.shop.service.executor.Executor;
import com.shop.service.validator.AlcoholPercentageValidator;
import com.shop.service.validator.BitternessValidator;
import com.shop.service.validator.ContainerTypeValidator;
import com.shop.service.validator.ContainerVolumeValidator;
import com.shop.service.validator.NotNullFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.AddPositionResponse;
import com.shop.servlet.request.AddPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class AddPositionService {
    private final PositionRepository positionRepository;
    private final List<Validator<AddPositionRequest>> positionRequestValidator;
    private final List<Validator<DraftBeerData>> draftBeerValidator;
    private final List<Validator<BottleBeerData>> bottleBeerValidator;
    private final List<Executor<AddPositionRequest, AddPositionResponse>> beerPositionExecutor;
    private final ValidatorService validatorService;

    public AddPositionService(PositionRepository positionRepository, Config config, ValidatorService validatorService) {
        this.positionRepository = positionRepository;
        this.validatorService = validatorService;
        positionRequestValidator = Arrays.asList(
                new NotNullFieldValidator<>(AddPositionRequest::getName, "Name is null or empty"),
                new NotNullFieldValidator<>(AddPositionRequest::getContainerType, "Container type is null or empty"),
                new NotNullFieldValidator<>(AddPositionRequest::getBeerType, "Beer type is null or empty"),
                new NotNullFieldValidator<>(AddPositionRequest::getAlcoholPercentage, "Alcohol percentage is null or empty"),
                new NotNullFieldValidator<>(AddPositionRequest::getBitterness, "Bitterness is null or empty"),
                new ContainerTypeValidator<>(AddPositionRequest::getContainerType, "Incorrect container type"),
                new AlcoholPercentageValidator(config.getMinAlcoholPercentage(), config.getMaxAlcoholPercentage(), "Incorrect alcohol percentage"),
                new BitternessValidator(config.getMinBitterness(), config.getMAxBitterness(), "Incorrect bitterness value")
        );
        draftBeerValidator = Collections.singletonList(
                new NotNullFieldValidator<>(DraftBeerData::getAvailableLiters, "Available litres is null or empty")
        );
        bottleBeerValidator = Arrays.asList(
                new NotNullFieldValidator<>(BottleBeerData::getContainerVolume, "Container volume is null or empty"),
                new NotNullFieldValidator<>(BottleBeerData::getQuantity, "Quantity is null or empty"),
                new ContainerVolumeValidator<>(BottleBeerData::getContainerVolume, config.getMinContainerVolume(), config.getMaxContainerVolume(), "Incorrect container volume")
        );
        beerPositionExecutor = Arrays.asList(
                new DraftBeerExecutor(validatorService, draftBeerValidator),
                new BottleBeerExecutor(validatorService, bottleBeerValidator)
        );

    }

    public AddPositionResponse add(AddPositionRequest addPositionRequest) {

        validatorService.validate(positionRequestValidator, addPositionRequest);

        if (positionRepository.getPositionByNameOrContainerType(addPositionRequest.getName(), addPositionRequest.getContainerType())) {
            throw new PositionAlreadyExistsException("Position already exists");
        }

        AddPositionResponse addPositionResponse = beerPositionExecutor.stream()
                .filter(n -> n.isValid(addPositionRequest.getContainerType()))
                .findFirst()
                .map(n -> n.execute(addPositionRequest))
                .orElseThrow(() -> new BeerPositionExecutorNotFoundException("Executor for this beer type not found"));

        return positionRepository.save(addPositionResponse);

    }
}
