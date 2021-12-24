package com.shop.service.performer;

import com.shop.model.BuyBottleBeerData;
import com.shop.model.BuyDraftBeerData;
import com.shop.service.validator.NotEmptyFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.BuyPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BuyDraftBeerDataValidatorPerformerTest {

    private BuyDraftBeerDataValidatorPerformer buyBottleBeerDataValidatorPerformer;

    private BuyPositionRequest buyPositionRequest;

    @BeforeEach
    public void setUp() {

        buyPositionRequest = new BuyPositionRequest();
        buyPositionRequest.setDraft(Collections.singletonList(new BuyDraftBeerData(null, 1.0)));

         List<Validator<BuyDraftBeerData>> buyDraftBeerDataValidator=Collections.singletonList(
                 new NotEmptyFieldValidator<>(BuyDraftBeerData::getId, "Id is null or empty")
         );

        buyBottleBeerDataValidatorPerformer = new BuyDraftBeerDataValidatorPerformer(buyDraftBeerDataValidator);
    }


    @Test
    void testPerformShouldReturnMessage1() {

        assertTrue(buyBottleBeerDataValidatorPerformer.isValid(buyPositionRequest));
    }

    @Test
    void testPerformShouldReturnMessage() {

        String result = "Id is null or empty";
        assertEquals(result, buyBottleBeerDataValidatorPerformer.perform(buyPositionRequest));
    }


    @Test
    void testPerformShouldReturnNull() {

        BuyBottleBeerDataValidatorPerformer buyBottleBeerDataValidatorPerformer = new BuyBottleBeerDataValidatorPerformer(
                new ArrayList<>());
        buyPositionRequest.setBottle(new ArrayList<>());

        assertNull(buyBottleBeerDataValidatorPerformer.perform(buyPositionRequest));
    }
}
