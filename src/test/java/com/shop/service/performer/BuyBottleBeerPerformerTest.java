package com.shop.service.performer;

import com.shop.model.BeerInfo;
import com.shop.model.BottleBeerData;
import com.shop.model.BuyBeerQuantity;
import com.shop.model.BuyBottleBeerData;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.repository.TransactionalHandler;
import com.shop.service.exception.AvailableQuantityExceededException;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.servlet.dto.BuyPositionDto;
import com.shop.servlet.dto.PositionDto;
import com.shop.servlet.request.BuyPositionRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BuyBottleBeerPerformerTest {

    private final PositionRepository positionRepository = Mockito.mock(PositionRepository.class);
    private final TransactionalHandler transactionalHandler = Mockito.mock(TransactionalHandler.class);

    private BuyBottleBeerPerformer buyBottleBeerPerformer;
    private final BuyPositionRequest buyPositionRequest = new BuyPositionRequest();
    private PositionDto positionDto;
    private final List<BuyPositionDto> buyPositionDtoList = new ArrayList<>();


    @BeforeEach
    public void setUp() {

        buyBottleBeerPerformer = new BuyBottleBeerPerformer(positionRepository,transactionalHandler);
        Position position = Position.builder()
                .beerInfo(new BeerInfo(1.0, 0))
                .build();
        positionDto = PositionDto.builder()
                .beerInfo(position.getBeerInfo())
                .build();



        buyPositionRequest.setBottle(List.of(new BuyBottleBeerData(1L, 0)));

        BuyPositionDto buyPositionDto = BuyPositionDto.builder()
                .positionDto(positionDto)
                .quantity(new BuyBeerQuantity(buyPositionRequest
                        .getBottle()
                        .get(0)
                        .getQuantity()))
                .quantityType("BottleBuyBeerQuantity")
                .build();
        buyPositionDtoList.add(buyPositionDto);
    }


    @Test
    void testIsValid() {
        assertTrue(buyBottleBeerPerformer.isValid(buyPositionRequest));
    }


    @Test
    void testPerformShouldThrowPositionNotFoundException() {
        buyPositionRequest.setBottle(List.of(new BuyBottleBeerData(1L, 1)));

        when(transactionalHandler.getCurrentSession()).thenReturn(any());
        when(positionRepository.findPositionById(1L,any())).thenReturn(Optional.empty());

        assertThrows(PositionNotFoundException.class, () ->
                buyBottleBeerPerformer.perform(buyPositionRequest));
    }


    @Test
    void testPerformShouldThrowAvailableQuantityExceededException() {
        buyPositionRequest.setBottle(List.of(new BuyBottleBeerData(1L, 1)));

        when(transactionalHandler.getCurrentSession()).thenReturn(any());
        when(positionRepository.findPositionById(1L,any())).thenReturn(Optional.ofNullable(positionDto));

        assertThrows(AvailableQuantityExceededException.class, () ->
                buyBottleBeerPerformer.perform(buyPositionRequest));
    }


    @Test
    void testPerformShouldReturnBuyPositionDtoList() {

        when(transactionalHandler.getCurrentSession()).thenReturn(any());
        when(positionRepository.findPositionById(1L, any())).thenReturn(Optional.ofNullable(positionDto));

        assertEquals(buyPositionDtoList, buyBottleBeerPerformer.perform(buyPositionRequest));
        verify(positionRepository, times(1)).findPositionById(any(), any());

    }
}

