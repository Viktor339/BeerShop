package com.shop.service.performer;

import com.shop.model.BottleBeerData;
import com.shop.service.ValidatorService;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.AddPositionDto;
import com.shop.servlet.request.AddPositionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BottleBeerPerformerTest {

    private static final String BOTTLE = "bottle";
    private final ValidatorService validatorService = mock(ValidatorService.class);

    private BottleBeerPerformer bottleBeerPerformer;
    private AddPositionRequest addPositionRequest;

    private AddPositionDto addPositionDto;


    @BeforeEach
    public void setUp() {

        List<Validator<BottleBeerData>> bottleBeerValidator = Collections.singletonList(
                (Validator<BottleBeerData>) Mockito.mock(Validator.class)
        );

        bottleBeerPerformer = new BottleBeerPerformer(validatorService, bottleBeerValidator);

        addPositionRequest = new AddPositionRequest();
        addPositionRequest.setName("name");
        addPositionRequest.setBeerInfo(new BottleBeerData(1.0, 1));

        BottleBeerData bottleBeerData = (BottleBeerData) addPositionRequest.getBeerInfo();

        addPositionDto = AddPositionDto.builder()
                .name(addPositionRequest.getName())
                .beerInfo(new BottleBeerData(bottleBeerData.getContainerVolume(), bottleBeerData.getQuantity()))
                .build();
    }


    @Test
    void testIsValid() {
        assertTrue(bottleBeerPerformer.isValid(BOTTLE));
    }

    @Test
    void testPerformShouldReturnAddPositionDto() {
        doNothing().when(validatorService).validate(any(), any());
        assertEquals(addPositionDto, bottleBeerPerformer.perform(addPositionRequest));
        verify(validatorService, times(1)).validate(any(), any());

    }
}

