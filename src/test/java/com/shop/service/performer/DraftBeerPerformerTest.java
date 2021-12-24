package com.shop.service.performer;

import com.shop.model.DraftBeerData;
import com.shop.service.ValidatorService;
import com.shop.service.validator.NotEmptyFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.AddPositionDto;
import com.shop.servlet.request.AddPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class DraftBeerPerformerTest {

    private final ValidatorService validatorService = mock(ValidatorService.class);
    private DraftBeerPerformer draftBeerPerformer;
    private AddPositionRequest addPositionRequest;
    private AddPositionDto addPositionDto;


    @BeforeEach
    public void setUp() {

        List<Validator<DraftBeerData>> draftBeerDataValidator = Collections.singletonList(
                new NotEmptyFieldValidator<>(DraftBeerData::getAvailableLiters, "Available litres is null or empty")
        );

        draftBeerPerformer = new DraftBeerPerformer(validatorService, draftBeerDataValidator);

        addPositionRequest = new AddPositionRequest();
        addPositionRequest.setName("name");
        addPositionRequest.setBeerInfo(new DraftBeerData(1.0));

        DraftBeerData draftBeerData = (DraftBeerData) addPositionRequest.getBeerInfo();

        addPositionDto = AddPositionDto.builder()
                .name(addPositionRequest.getName())
                .beerInfo(new DraftBeerData(draftBeerData.getAvailableLiters()))
                .build();
    }


    @Test
    void testIsValid() {

        assertTrue(draftBeerPerformer.isValid("draft"));
    }

    @Test
    void testPerformShouldReturnAddPositionDto() {
        doNothing().when(validatorService).validate(any(), any());
        assertEquals(addPositionDto, draftBeerPerformer.perform(addPositionRequest));
        verify(validatorService, times(1)).validate(any(), any());

    }

}
