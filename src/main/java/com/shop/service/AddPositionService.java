package com.shop.service;

import com.shop.repository.PositionRepository;
import com.shop.service.exception.BeerPositionExecutorNotFoundException;
import com.shop.service.exception.PositionAlreadyExistsException;
import com.shop.service.performer.Performer;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.AddPositionDto;
import com.shop.servlet.dto.AddPositionResponse;
import com.shop.servlet.request.AddPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AddPositionService {
    private final PositionRepository positionRepository;
    private final List<Validator<AddPositionRequest>> positionRequestValidator;
    private final List<Performer<AddPositionRequest, AddPositionDto>> beerPositionPerformer;
    private final ValidatorService validatorService;

    public AddPositionService(PositionRepository positionRepository,
                              ValidatorService validatorService,
                              List<Validator<AddPositionRequest>> positionRequestValidator,
                              List<Performer<AddPositionRequest, AddPositionDto>> beerPositionPerformer) {
        this.positionRepository = positionRepository;
        this.validatorService = validatorService;
        this.positionRequestValidator = positionRequestValidator;
        this.beerPositionPerformer = beerPositionPerformer;

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
