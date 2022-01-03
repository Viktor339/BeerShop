package com.shop.service.performer;

import com.shop.model.BuyDraftBeerData;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.BuyPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class BuyDraftBeerDataValidatorPerformerTest {

    private static final String RESULT = "Id is null or empty";
    private BuyDraftBeerDataValidatorPerformer buyBottleBeerDataValidatorPerformer;
    private BuyPositionRequest buyPositionRequest;
    private List<Validator<BuyDraftBeerData>> buyDraftBeerDataValidator;

    @BeforeEach
    public void setUp() {

        buyPositionRequest = new BuyPositionRequest();
        buyPositionRequest.setDraft(List.of(new BuyDraftBeerData(null, 1.0)));

        buyDraftBeerDataValidator = List.of(
                (Validator<BuyDraftBeerData>) Mockito.mock(Validator.class)
        );

        buyBottleBeerDataValidatorPerformer = new BuyDraftBeerDataValidatorPerformer(buyDraftBeerDataValidator);
    }


    @Test
    void testIsValid() {

        assertTrue(buyBottleBeerDataValidatorPerformer.isValid(buyPositionRequest));
    }

    @Test
    void testPerformShouldReturnMessage() {

        when(buyDraftBeerDataValidator.get(0).isValid(buyPositionRequest.getDraft().get(0))).thenReturn(true);
        when(buyDraftBeerDataValidator.get(0).getMessage()).thenReturn(RESULT);
        assertEquals(RESULT, buyBottleBeerDataValidatorPerformer.perform(buyPositionRequest));
    }


    @Test
    void testPerformShouldReturnNull() {

        BuyBottleBeerDataValidatorPerformer buyBottleBeerDataValidatorPerformer = new BuyBottleBeerDataValidatorPerformer(
                new ArrayList<>());
        buyPositionRequest.setBottle(new ArrayList<>());

        assertNull(buyBottleBeerDataValidatorPerformer.perform(buyPositionRequest));
    }
}
