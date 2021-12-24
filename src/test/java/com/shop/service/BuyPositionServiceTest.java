package com.shop.service;

import com.shop.model.BottleBeerData;
import com.shop.model.BuyBottleBeerData;
import com.shop.model.BuyDraftBeerData;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.repository.UserRepository;
import com.shop.repository.UserTransactionRepository;
import com.shop.service.exception.ValidatorException;
import com.shop.servlet.request.BuyPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    @BeforeEach
    public void setUp() {
        buyPositionRequest = new BuyPositionRequest();
        buyPositionService = new BuyPositionService(positionRepository, userTransactionRepository, userRepository);

        buyPositionRequest.setDraft(Collections.singletonList(new BuyDraftBeerData(null, 1.0)));
        uuid = 1;

    }

    @Test
    void testBuyShouldThrowValidatorException() {

        buyPositionRequest.setBottle(Collections.singletonList(new BuyBottleBeerData(null, 1)));
        buyPositionRequest.setDraft(Collections.emptyList());

        assertThrows(ValidatorException.class, () ->
                buyPositionService.buy(buyPositionRequest, uuid));

    }


    @Test
    void testBuy() {

        buyPositionRequest.setBottle(Collections.singletonList(new BuyBottleBeerData(1L, 1)));
        buyPositionRequest.setDraft(Collections.emptyList());

        when(userRepository.getUserIdByUUID(uuid)).thenReturn(1);
        when(positionRepository.findPositionById(1L, BottleBeerData.class)).thenReturn(Optional.ofNullable(Position.builder()
                .beerInfo(new BottleBeerData(1.0, 2))
                .build()));

        buyPositionService.buy(buyPositionRequest, uuid);

        verify(userRepository, times(1)).getUserIdByUUID(uuid);
        verify(positionRepository, times(1)).updatePositionAfterPurchase(any());
        verify(userTransactionRepository, times(1)).save(Mockito.argThat(bpd -> bpd.getUserId() == 1));
    }
}

