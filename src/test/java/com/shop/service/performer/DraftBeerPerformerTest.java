package com.shop.service.performer;

import com.shop.model.BeerInfo;
import com.shop.service.ValidatorService;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.AddPositionDto;
import com.shop.servlet.request.AddPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

        List<Validator<BeerInfo>> draftBeerDataValidator = List.of(
                (Validator<BeerInfo>) Mockito.mock(Validator.class)
        );

        draftBeerPerformer = new DraftBeerPerformer(validatorService, draftBeerDataValidator);

        addPositionRequest = new AddPositionRequest();
        addPositionRequest.setName("name");
        addPositionRequest.setBeerInfo(new BeerInfo(1.0));

        BeerInfo draftBeerData = addPositionRequest.getBeerInfo();

        addPositionDto = AddPositionDto.builder()
                .name(addPositionRequest.getName())
                .beerInfo(new BeerInfo(draftBeerData.getAvailableLiters()))
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
