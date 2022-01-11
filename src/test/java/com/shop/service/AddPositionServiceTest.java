package com.shop.service;

import com.shop.model.BeerInfo;
import com.shop.repository.PositionRepository;
import com.shop.repository.TransactionalHandler;
import com.shop.service.exception.BeerPositionExecutorNotFoundException;
import com.shop.service.exception.PositionAlreadyExistsException;
import com.shop.service.performer.Performer;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.AddPositionDto;
import com.shop.servlet.dto.AddPositionResponse;
import com.shop.servlet.request.AddPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AddPositionServiceTest {

    private final PositionRepository positionRepository = mock(PositionRepository.class);
    private final ValidatorService validatorService = mock(ValidatorService.class);
    private List<Performer<AddPositionRequest, AddPositionDto>> beerPositionPerformer;


    private final List<Validator<AddPositionRequest>> positionRequestValidator = new ArrayList<>();
    private AddPositionRequest addPositionRequest;
    private AddPositionService addPositionService;
    private AddPositionResponse addPositionResponse;
    private final AtomicReference<AddPositionDto> addPositionDtoAtomic = new AtomicReference<>();
    private AddPositionDto addPositionDto;
    private final TransactionalHandler transactionalHandler = new TransactionalHandler();


    @BeforeEach
    public void setUp() {

        beerPositionPerformer = List.of(
                (Performer<AddPositionRequest, AddPositionDto>) mock(Performer.class));

        addPositionService = new AddPositionService(positionRepository, validatorService, positionRequestValidator, beerPositionPerformer, transactionalHandler);

        addPositionRequest = new AddPositionRequest();
        addPositionRequest.setName("name");
        addPositionRequest.setContainerType("type");
        addPositionRequest.setBeerInfo(new BeerInfo(2.0));

        addPositionDto = AddPositionDto.builder()
                .id(1)
                .name("name")
                .beerType("beerType")
                .alcoholPercentage(2.6)
                .bitterness(1)
                .containerType("containerType")
                .beerInfo(new BeerInfo(2.0, 1))
                .build();

        addPositionDtoAtomic.set(addPositionDto);

        addPositionResponse = AddPositionResponse.builder()
                .id(addPositionDtoAtomic.get().getId())
                .name(addPositionDtoAtomic.get().getName())
                .beerType(addPositionDtoAtomic.get().getBeerType())
                .alcoholPercentage(addPositionDtoAtomic.get().getAlcoholPercentage())
                .bitterness(addPositionDtoAtomic.get().getBitterness())
                .containerType(addPositionDtoAtomic.get().getContainerType())
                .beerInfo(addPositionDtoAtomic.get().getBeerInfo())
                .build();

    }

    @Test
    void testAddShouldThrowPositionAlreadyExistsException() {

        doNothing().when(validatorService).validate(positionRequestValidator, addPositionRequest);

        assertThrows(PositionAlreadyExistsException.class,
                () -> transactionalHandler.doTransaction(session -> {

                    when(positionRepository.existsPositionByNameAndContainerType(addPositionRequest.getName(),
                            addPositionRequest.getContainerType(),
                            session)).thenReturn(true);

                    addPositionService.add(addPositionRequest);

                }));
    }

    @Test
    void testAddShouldThrowBeerPositionExecutorNotFoundException() {

        doNothing().when(validatorService).validate(positionRequestValidator, addPositionRequest);

        assertThrows(BeerPositionExecutorNotFoundException.class,
                () -> transactionalHandler.doTransaction(session -> {

                    when(positionRepository.existsPositionByNameAndContainerType(addPositionRequest.getName(),
                            addPositionRequest.getContainerType(),
                            session)).thenReturn(false);

                    addPositionService.add(addPositionRequest);

                }));
    }


    @Test
    void testAddShouldReturnAddPositionResponse() {

        addPositionRequest.setName("name");
        addPositionRequest.setContainerType("draft");

        transactionalHandler.doTransaction(session -> {

            when(positionRepository.existsPositionByNameAndContainerType("name", "type", session)).thenReturn(false);
            when(positionRepository.save(addPositionDto, session)).thenReturn(addPositionDto);
            when(beerPositionPerformer.get(0).isValid(addPositionRequest.getContainerType())).thenReturn(true);
            when(beerPositionPerformer.get(0).perform(addPositionRequest)).thenReturn(addPositionDto);

            assertEquals(addPositionResponse,
                    addPositionService.add(addPositionRequest));
            transactionalHandler.beginTransaction();
        });

    }
}

