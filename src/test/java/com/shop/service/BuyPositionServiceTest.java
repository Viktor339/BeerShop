package com.shop.service;

import com.shop.model.BuyBeerQuantity;
import com.shop.model.BuyBottleBeerData;
import com.shop.model.BuyDraftBeerData;
import com.shop.repository.PositionRepository;
import com.shop.repository.TransactionalHandler;
import com.shop.repository.UserRepository;
import com.shop.repository.UserTransactionRepository;
import com.shop.service.exception.ValidatorException;
import com.shop.service.performer.Performer;
import com.shop.servlet.dto.BuyPositionDto;
import com.shop.servlet.dto.PositionDto;
import com.shop.servlet.request.BuyPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BuyPositionServiceTest {

    private final PositionRepository positionRepository = mock(PositionRepository.class);
    private final UserTransactionRepository userTransactionRepository = mock(UserTransactionRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private BuyPositionService buyPositionService;
    private BuyPositionRequest buyPositionRequest;
    private Object uuid;
    private List<Performer<BuyPositionRequest, String>> buyBeerDataValidatorPerformer;
    private List<Performer<BuyPositionRequest, List<BuyPositionDto>>> buyBeerPerformer;
    private List<BuyPositionDto> buyPositionDtoList;
    private final TransactionalHandler transactionalHandler = new TransactionalHandler();
    private PositionDto positionDto;
    private BuyPositionDto buyPositionDto;

    @BeforeEach
    public void setUp() {

        buyBeerDataValidatorPerformer = List.of(
                (Performer<BuyPositionRequest, String>) Mockito.mock(Performer.class));

        buyBeerPerformer = List.of(
                (Performer<BuyPositionRequest, List<BuyPositionDto>>) Mockito.mock(Performer.class)
        );

        buyPositionRequest = new BuyPositionRequest();
        buyPositionService = new BuyPositionService(buyBeerPerformer,
                positionRepository,
                userTransactionRepository,
                userRepository,
                buyBeerDataValidatorPerformer,
                transactionalHandler);

        buyPositionRequest.setDraft(List.of(new BuyDraftBeerData(null, 1.0)));
        uuid = 1;


        buyPositionDtoList = new ArrayList<>();

        positionDto = PositionDto.builder().build();

        buyPositionDto = BuyPositionDto.builder()
                .positionDto(positionDto)
                .userId(1)
                .quantity(new BuyBeerQuantity(1.0))
                .quantityType("BottleBuyBeerQuantity")
                .build();

        buyPositionDtoList.add(buyPositionDto);

    }

    @Test
    void testBuyShouldThrowValidatorException() {

        buyPositionRequest.setBottle(List.of(new BuyBottleBeerData(null, 1)));
        buyPositionRequest.setDraft(List.of());

        when(buyBeerDataValidatorPerformer.get(0).isValid(buyPositionRequest)).thenReturn(true);
        when(buyBeerDataValidatorPerformer.get(0).perform(buyPositionRequest)).thenReturn("message");

        assertThrows(ValidatorException.class, () ->
                buyPositionService.buy(buyPositionRequest, uuid));
    }


    @Test
    void testBuy() {

        buyPositionRequest.setBottle(List.of(new BuyBottleBeerData(1L, 1)));
        buyPositionRequest.setDraft(List.of());

        transactionalHandler.doTransaction(session -> {

            when(userRepository.getUserIdByUUID(uuid, session)).thenReturn(1);
            when(buyBeerPerformer.get(0).isValid(buyPositionRequest)).thenReturn(true);
            when(buyBeerPerformer.get(0).perform(buyPositionRequest)).thenReturn(buyPositionDtoList);

            buyPositionService.buy(buyPositionRequest, uuid);

            verify(userRepository, times(1)).getUserIdByUUID(uuid, session);
            verify(positionRepository, times(1)).updatePositionAfterPurchase(positionDto, session);
            verify(userTransactionRepository, times(1)).save(buyPositionDto, session);

            transactionalHandler.beginTransaction();

        });

    }
}

