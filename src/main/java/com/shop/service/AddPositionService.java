package com.shop.service;

import com.shop.repository.PositionRepository;
import com.shop.repository.TransactionalHandler;
import com.shop.service.exception.BeerPositionExecutorNotFoundException;
import com.shop.service.exception.PositionAlreadyExistsException;
import com.shop.service.performer.Performer;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.AddPositionDto;
import com.shop.servlet.dto.AddPositionResponse;
import com.shop.servlet.request.AddPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class AddPositionService {
    private final PositionRepository positionRepository;
    private final List<Validator<AddPositionRequest>> positionRequestValidator;
    private final List<Performer<AddPositionRequest, AddPositionDto>> beerPositionPerformer;
    private final ValidatorService validatorService;
    private TransactionalHandler transactionalHandler;

    public AddPositionService(PositionRepository positionRepository,
                              ValidatorService validatorService,
                              List<Validator<AddPositionRequest>> positionRequestValidator,
                              List<Performer<AddPositionRequest, AddPositionDto>> beerPositionPerformer,
                              TransactionalHandler transactionalHandler) {
        this.positionRepository = positionRepository;
        this.validatorService = validatorService;
        this.positionRequestValidator = positionRequestValidator;
        this.beerPositionPerformer = beerPositionPerformer;
        this.transactionalHandler = transactionalHandler;

    }

    public AddPositionResponse add(AddPositionRequest addPositionRequest) {

        validatorService.validate(positionRequestValidator, addPositionRequest);

        AtomicReference<AddPositionDto> addPositionDto = new AtomicReference<>();

        transactionalHandler.doTransaction((session) -> {

            if (positionRepository.existsPositionByNameAndContainerType(addPositionRequest.getName(), addPositionRequest.getContainerType(), session)) {
                throw new PositionAlreadyExistsException("Position already exists");
            }

            addPositionDto.set(
                    positionRepository.save(beerPositionPerformer.stream()
                            .filter(n -> n.isValid(addPositionRequest.getContainerType()))
                            .findFirst()
                            .map(n -> n.perform(addPositionRequest))
                            .orElseThrow(() -> new BeerPositionExecutorNotFoundException("Executor for this beer type not found")), session));
        });

        return AddPositionResponse.builder()
                .id(addPositionDto.get().getId())
                .name(addPositionDto.get().getName())
                .beerType(addPositionDto.get().getBeerType())
                .alcoholPercentage(addPositionDto.get().getAlcoholPercentage())
                .bitterness(addPositionDto.get().getBitterness())
                .containerType(addPositionDto.get().getContainerType())
                .beerInfo(addPositionDto.get().getBeerInfo())
                .build();
    }
}
