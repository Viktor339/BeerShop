package com.shop.service;

import com.shop.config.Config;
import com.shop.model.BottleBeerData;
import com.shop.model.DraftBeerData;
import com.shop.repository.PositionRepository;
import com.shop.service.exception.BeerPositionExecutorNotFoundException;
import com.shop.service.exception.PositionAlreadyExistsException;
import com.shop.service.performer.BottleBeerPerformer;
import com.shop.service.performer.DraftBeerPerformer;
import com.shop.service.performer.Performer;
import com.shop.service.validator.AlcoholPercentageValidator;
import com.shop.service.validator.BitternessValidator;
import com.shop.service.validator.ContainerTypeValidator;
import com.shop.service.validator.ContainerVolumeValidator;
import com.shop.service.validator.NotEmptyFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.AddPositionDto;
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
    private final List<Performer<AddPositionRequest, AddPositionDto>> beerPositionPerformer;
    private final ValidatorService validatorService;

    public AddPositionService(PositionRepository positionRepository, Config config, ValidatorService validatorService) {
        this.positionRepository = positionRepository;
        this.validatorService = validatorService;
        positionRequestValidator = Arrays.asList(
                new NotEmptyFieldValidator<>(AddPositionRequest::getName, "Name is null or empty"),
                new NotEmptyFieldValidator<>(AddPositionRequest::getContainerType, "Container type is null or empty"),
                new NotEmptyFieldValidator<>(AddPositionRequest::getBeerType, "Beer type is null or empty"),
                new NotEmptyFieldValidator<>(AddPositionRequest::getAlcoholPercentage, "Alcohol percentage is null or empty"),
                new NotEmptyFieldValidator<>(AddPositionRequest::getBitterness, "Bitterness is null or empty"),
                new ContainerTypeValidator<>(AddPositionRequest::getContainerType, "Incorrect container type"),
                new AlcoholPercentageValidator(config.getMinAlcoholPercentage(), config.getMaxAlcoholPercentage(), "Incorrect alcohol percentage"),
                new BitternessValidator(config.getMinBitterness(), config.getMAxBitterness(), "Incorrect bitterness value")
        );
        draftBeerValidator = Collections.singletonList(
                new NotEmptyFieldValidator<>(DraftBeerData::getAvailableLiters, "Available litres is null or empty")
        );
        bottleBeerValidator = Arrays.asList(
                new NotEmptyFieldValidator<>(BottleBeerData::getContainerVolume, "Container volume is null or empty"),
                new NotEmptyFieldValidator<>(BottleBeerData::getQuantity, "Quantity is null or empty"),
                new ContainerVolumeValidator<>(BottleBeerData::getContainerVolume, config.getMinContainerVolume(), config.getMaxContainerVolume(), "Incorrect container volume")
        );
        beerPositionPerformer = Arrays.asList(
                new DraftBeerPerformer(validatorService, draftBeerValidator),
                new BottleBeerPerformer(validatorService, bottleBeerValidator)
        );

    }

    public AddPositionResponse add(AddPositionRequest addPositionRequest) {

        validatorService.validate(positionRequestValidator, addPositionRequest);

        if (positionRepository.existsPositionByNameAndContainerType(addPositionRequest.getName(), addPositionRequest.getContainerType())) {
            throw new PositionAlreadyExistsException("Position already exists");
        }

        AddPositionDto addPositionDto = positionRepository.save(beerPositionPerformer.stream()
                .filter(n -> n.isValid(addPositionRequest.getContainerType()))
                .findFirst()
                .map(n -> n.perform(addPositionRequest))
                .orElseThrow(() -> new BeerPositionExecutorNotFoundException("Executor for this beer type not found")));


        return AddPositionResponse.builder()
                .id(addPositionDto.getId())
                .name(addPositionDto.getName())
                .beerType(addPositionDto.getBeerType())
                .alcoholPercentage(addPositionDto.getAlcoholPercentage())
                .bitterness(addPositionDto.getBitterness())
                .containerType(addPositionDto.getContainerType())
                .beerInfo(addPositionDto.getBeerInfo())
                .build();
    }
}
