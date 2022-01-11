package com.shop.service;

import com.shop.model.BottleBeerData;
import com.shop.repository.PositionRepository;
import com.shop.repository.TransactionalHandler;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.servlet.dto.ChangePositionResponse;
import com.shop.servlet.request.ChangePositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private final TransactionalHandler transactionalHandler = new TransactionalHandler();
    private ChangePositionResponse changePositionResponse;
    private BottleBeerData bottleBeerData;


    @BeforeEach
    public void setUp() {
        changePositionService = new ChangePositionService(positionRepository, validatorService, new ArrayList<>(), transactionalHandler);

        changePositionRequest = new ChangePositionRequest();
        changePositionRequest.setId(1);
        changePositionRequest.setQuantity(1);
        changePositionRequest.setContainerVolume(1.0);

        changePositionResponse = ChangePositionResponse.builder()
                .id(1L)
                .beerInfo(new BottleBeerData(1.0, 1))
                .build();

        bottleBeerData = BottleBeerData.builder().build();
    }


    @Test
    void testChangeShouldThrowPositionNotFoundException() {

        transactionalHandler.doTransaction(session -> {

            when(positionRepository.existsPositionById(changePositionRequest.getId(), session)).thenReturn(false);
            assertThrows(PositionNotFoundException.class, () ->
                    changePositionService.change(changePositionRequest));

            transactionalHandler.beginTransaction();
        });


    }

    @Test
    void testChangeShouldReturnChangePositionResponse() {

        transactionalHandler.doTransaction(session -> {

            doNothing().when(validatorService).validate(new ArrayList<>(), changePositionRequest);
            when(positionRepository.existsPositionById(1, session)).thenReturn(true);
            doNothing().when(positionRepository).update(bottleBeerData, 1, session);

            assertEquals(changePositionResponse, changePositionService.change(changePositionRequest));
            verify(validatorService, times(1)).validate(new ArrayList<>(), changePositionRequest);
            verify(positionRepository, times(1)).existsPositionById(1, session);

            transactionalHandler.beginTransaction();
        });

    }
}

