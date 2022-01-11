package com.shop.service.performer;

import com.shop.model.BeerInfo;
import com.shop.model.BuyBeerQuantity;
import com.shop.model.BuyBottleBeerData;
import com.shop.model.BuyDraftBeerData;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.repository.TransactionalHandler;
import com.shop.service.exception.AvailableQuantityExceededException;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.servlet.dto.BuyPositionDto;
import com.shop.servlet.dto.PositionDto;
import com.shop.servlet.request.BuyPositionRequest;
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

public class BuyDraftBeerPerformerTest {

    private final PositionRepository positionRepository = Mockito.mock(PositionRepository.class);
    private final TransactionalHandler transactionalHandler = Mockito.mock(TransactionalHandler.class);

    private BuyDraftBeerPerformer buyDraftBeerPerformer;
    private final BuyPositionRequest buyPositionRequest = new BuyPositionRequest();
    private final List<BuyPositionDto> buyPositionDtoList = new ArrayList<>();
    private PositionDto positionDto;

    @BeforeEach
    public void setUp() {

        buyDraftBeerPerformer = new BuyDraftBeerPerformer(positionRepository, transactionalHandler);
        Position position = Position.builder()
                .beerInfo(new BeerInfo(0.0))
                .build();

        positionDto = PositionDto.builder()
                .beerInfo(position.getBeerInfo())
                .build();

        buyPositionRequest.setDraft(List.of(new BuyDraftBeerData(1L, 0.0)));

        BuyPositionDto buyPositionDto = BuyPositionDto.builder()
                .positionDto(positionDto)
                .quantity(new BuyBeerQuantity(buyPositionRequest
                        .getDraft()
                        .get(0)
                        .getQuantity()))
                .quantityType("DraftBuyBeerQuantity")
                .build();
        buyPositionDtoList.add(buyPositionDto);
    }


    @Test
    void testIsValid() {
        assertTrue(buyDraftBeerPerformer.isValid(buyPositionRequest));
    }

    @Test
    void testPerformShouldThrowPositionNotFoundException() {
        buyPositionRequest.setBottle(List.of(new BuyBottleBeerData(1L, 1)));
        when(transactionalHandler.getCurrentSession()).thenReturn(any());

        when(positionRepository.findPositionById(1L, any())).thenReturn(Optional.empty());

        assertThrows(PositionNotFoundException.class, () ->
                buyDraftBeerPerformer.perform(buyPositionRequest));
    }

    @Test
    void testPerformShouldThrowAvailableQuantityExceededException() {
        buyPositionRequest.setDraft(List.of(new BuyDraftBeerData(1L, 1.0)));
        when(transactionalHandler.getCurrentSession()).thenReturn(any());

        when(positionRepository.findPositionById(1L, any())).thenReturn(Optional.of(
                positionDto
        ));

        assertThrows(AvailableQuantityExceededException.class, () ->
                buyDraftBeerPerformer.perform(buyPositionRequest));
    }

    @Test
    void testPerformShouldReturnBuyPositionDtoList() {

        when(transactionalHandler.getCurrentSession()).thenReturn(any());

        when(positionRepository.findPositionById(1L, any())).thenReturn(Optional.ofNullable(
                positionDto
        ));

        assertEquals(buyPositionDtoList, buyDraftBeerPerformer.perform(buyPositionRequest));
        verify(positionRepository, times(1)).findPositionById(any(), any());

    }
}
