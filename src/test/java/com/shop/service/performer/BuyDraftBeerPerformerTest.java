package com.shop.service.performer;

import com.shop.model.BottleBeerData;
import com.shop.model.BuyBottleBeerData;
import com.shop.model.BuyDraftBeerData;
import com.shop.model.DraftBeerData;
import com.shop.model.DraftBuyBeerQuantity;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.service.exception.AvailableQuantityExceededException;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.servlet.dto.BuyPositionDto;
import com.shop.servlet.request.BuyPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
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
    private BuyDraftBeerPerformer buyDraftBeerPerformer;
    private final BuyPositionRequest buyPositionRequest = new BuyPositionRequest();
    private Position position;
    private final List<BuyPositionDto> buyPositionDtoList = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        buyDraftBeerPerformer = new BuyDraftBeerPerformer(positionRepository);
        position = Position.builder()
                .beerInfo(new DraftBeerData(0.0))
                .build();

        buyPositionRequest.setDraft(Collections.singletonList(new BuyDraftBeerData(1L, 0.0)));

        BuyPositionDto buyPositionDto = BuyPositionDto.builder()
                .position(position)
                .quantity(new DraftBuyBeerQuantity(buyPositionRequest
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
        buyPositionRequest.setBottle(Collections.singletonList(new BuyBottleBeerData(1L, 1)));

        when(positionRepository.findPositionById(1L, BottleBeerData.class)).thenReturn(Optional.empty());

        assertThrows(PositionNotFoundException.class, () ->
                buyDraftBeerPerformer.perform(buyPositionRequest));
    }

    @Test
    void testPerformShouldThrowAvailableQuantityExceededException() {
        buyPositionRequest.setDraft(Collections.singletonList(new BuyDraftBeerData(1L, 1.0)));

        when(positionRepository.findPositionById(1L, DraftBeerData.class)).thenReturn(Optional.ofNullable(
                position
        ));

        assertThrows(AvailableQuantityExceededException.class, () ->
                buyDraftBeerPerformer.perform(buyPositionRequest));
    }

    @Test
    void testPerformShouldReturnBuyPositionDtoList() {

        when(positionRepository.findPositionById(1L, DraftBeerData.class)).thenReturn(Optional.ofNullable(
                position
        ));

        assertEquals(buyPositionDtoList, buyDraftBeerPerformer.perform(buyPositionRequest));
        verify(positionRepository, times(1)).findPositionById(any(), any());

    }
}
