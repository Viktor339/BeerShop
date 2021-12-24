package com.shop.service;

import com.shop.config.Config;
import com.shop.model.BottleBeerData;
import com.shop.model.DraftBeerData;
import com.shop.repository.PositionRepository;
import com.shop.service.exception.BeerPositionExecutorNotFoundException;
import com.shop.service.exception.PositionAlreadyExistsException;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.AddPositionDto;
import com.shop.servlet.dto.AddPositionResponse;
import com.shop.servlet.request.AddPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddPositionServiceTest {

    private final PositionRepository positionRepository = mock(PositionRepository.class);
    private final ValidatorService validatorService = mock(ValidatorService.class);
    private final Config config = mock(Config.class);

    private final List<Validator<AddPositionRequest>> positionRequestValidator = new ArrayList<>();
    private AddPositionRequest addPositionRequest;
    private AddPositionService addPositionService;
    private AddPositionDto addPositionDto;
    private AddPositionResponse addPositionResponse;


    @BeforeEach
    public void setUp() {

        addPositionService = new AddPositionService(positionRepository, config, validatorService);

        addPositionRequest = new AddPositionRequest();
        addPositionRequest.setName("name");
        addPositionRequest.setContainerType("type");
        addPositionRequest.setBeerInfo(new DraftBeerData(2.0));

        addPositionDto = AddPositionDto.builder()
                .id(1)
                .name("name")
                .beerType("beerType")
                .alcoholPercentage(2.6)
                .bitterness(1)
                .containerType("containerType")
                .beerInfo(new BottleBeerData(2.0, 1))
                .build();

        addPositionResponse = AddPositionResponse.builder()
                .id(addPositionDto.getId())
                .name(addPositionDto.getName())
                .beerType(addPositionDto.getBeerType())
                .alcoholPercentage(addPositionDto.getAlcoholPercentage())
                .bitterness(addPositionDto.getBitterness())
                .containerType(addPositionDto.getContainerType())
                .beerInfo(addPositionDto.getBeerInfo())
                .build();
    }


    @Test
    void testAddShouldThrowPositionAlreadyExistsException() {

        doNothing().when(validatorService).validate(positionRequestValidator, addPositionRequest);
        when(positionRepository.existsPositionByNameAndContainerType(addPositionRequest.getName(), addPositionRequest.getContainerType()))
                .thenThrow(new PositionAlreadyExistsException("Position already exists"));

        assertThrows(PositionAlreadyExistsException.class, () ->
                addPositionService.add(addPositionRequest));
    }

    @Test
    void testAddShouldThrowBeerPositionExecutorNotFoundException() {

        doNothing().when(validatorService).validate(positionRequestValidator, addPositionRequest);
        when(positionRepository.existsPositionByNameAndContainerType(any(), any())).thenReturn(false);

        assertThrows(BeerPositionExecutorNotFoundException.class, () ->
                addPositionService.add(addPositionRequest));
    }

    @Test
    void testAddShouldReturnAddPositionResponse() {

        addPositionRequest.setName("name");
        addPositionRequest.setContainerType("draft");

        doNothing().when(validatorService).validate(positionRequestValidator, addPositionRequest);
        when(positionRepository.existsPositionByNameAndContainerType(any(), any())).thenReturn(false);
        when(positionRepository.save(any())).thenReturn(addPositionDto);

        assertEquals(addPositionResponse, addPositionService.add(addPositionRequest));
        verify(validatorService, times(2)).validate(any(), any());
        verify(positionRepository, times(1)).existsPositionByNameAndContainerType(any(), any());
        verify(positionRepository, times(1)).save(any());

    }
}

