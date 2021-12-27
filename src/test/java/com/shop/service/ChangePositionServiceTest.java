package com.shop.service;

import com.shop.model.BottleBeerData;
import com.shop.repository.PositionRepository;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.servlet.dto.ChangePositionResponse;
import com.shop.servlet.request.ChangePositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChangePositionServiceTest {


    private final PositionRepository positionRepository = mock(PositionRepository.class);
    private final ValidatorService validatorService = mock(ValidatorService.class);

    private ChangePositionService changePositionService;
    private ChangePositionRequest changePositionRequest;

    private ChangePositionResponse changePositionResponse;


    @BeforeEach
    public void setUp() {
        changePositionService = new ChangePositionService(positionRepository, validatorService, new ArrayList<>());

        changePositionRequest = new ChangePositionRequest();
        changePositionRequest.setId(1L);
        changePositionRequest.setQuantity(1);
        changePositionRequest.setContainerVolume(1.0);

        changePositionResponse = ChangePositionResponse.builder()
                .id(1L)
                .beerInfo(new BottleBeerData(1.0, 1))
                .build();
    }


    @Test
    void testChangeShouldThrowPositionNotFoundException() {

        when(positionRepository.existsPositionById(changePositionRequest.getId())).thenReturn(false);
        assertThrows(PositionNotFoundException.class, () ->
                changePositionService.change(changePositionRequest));
    }

    @Test
    void testChangeShouldReturnChangePositionResponse() {

        doNothing().when(validatorService).validate(new ArrayList<>(), changePositionRequest);
        when(positionRepository.existsPositionById(any())).thenReturn(true);
        doNothing().when(positionRepository).update(any(), any());

        assertEquals(changePositionResponse, changePositionService.change(changePositionRequest));
        verify(validatorService, times(1)).validate(any(), any());
        verify(positionRepository, times(1)).existsPositionById(any());
        verify(positionRepository, times(1)).update(any(), any());
    }
}

