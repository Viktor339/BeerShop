package com.shop.service.performer;

import com.shop.model.DraftBeerData;
import com.shop.service.ValidatorService;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.AddPositionDto;
import com.shop.servlet.dto.AddPositionResponse;
import com.shop.servlet.request.AddPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DraftBeerPerformer implements Performer<AddPositionRequest, AddPositionDto> {
    private final ValidatorService validatorService;
    private final List<Validator<DraftBeerData>> draftBeerValidator;

    @Override
    public boolean isValid(Object value) {
        return value.equals("draft");
    }

    @Override
    public AddPositionDto perform(AddPositionRequest addPositionRequest) {

        DraftBeerData draftBeerData = (DraftBeerData) addPositionRequest.getBeerInfo();
        validatorService.validate(draftBeerValidator, draftBeerData);



        return AddPositionDto.builder()
                .name(addPositionRequest.getName())
                .beerType(addPositionRequest.getBeerType())
                .alcoholPercentage(addPositionRequest.getAlcoholPercentage())
                .bitterness(addPositionRequest.getBitterness())
                .containerType(addPositionRequest.getContainerType())
                .beerInfo(new DraftBeerData(draftBeerData.getAvailableLiters()))
                .build();
    }
}
