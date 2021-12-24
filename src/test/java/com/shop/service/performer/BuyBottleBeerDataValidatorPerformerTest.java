package com.shop.service.performer;

import com.shop.model.BuyBottleBeerData;
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

class BuyBottleBeerDataValidatorPerformerTest {

    private BuyBottleBeerDataValidatorPerformer buyBottleBeerDataValidatorPerformer;

    private BuyPositionRequest buyPositionRequest;

    @BeforeEach
    public void setUp() {

        buyPositionRequest = new BuyPositionRequest();
        buyPositionRequest.setBottle(Collections.singletonList(new BuyBottleBeerData(null, 1)));

        List<Validator<BuyBottleBeerData>> buyBottleBeerDataValidator = Collections.singletonList(
                new NotEmptyFieldValidator<>(BuyBottleBeerData::getId, "Id is null or empty")
        );

        buyBottleBeerDataValidatorPerformer = new BuyBottleBeerDataValidatorPerformer(buyBottleBeerDataValidator);
    }


    @Test
    void testPerformShouldReturnTrue() {

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

