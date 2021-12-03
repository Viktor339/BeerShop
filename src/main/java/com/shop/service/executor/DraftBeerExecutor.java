package com.shop.service.executor;

import com.shop.model.DraftBeerData;
import com.shop.service.ValidatorService;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.AddPositionResponse;
import com.shop.servlet.request.AddPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class DraftBeerExecutor implements Executor<AddPositionRequest, AddPositionResponse> {
    private final ValidatorService validatorService;
    private final List<Validator<DraftBeerData>> draftBeerValidator;

    @Override
    public boolean isValid(String value) {
        return value.equals("draft");
    }

    @Override
    public AddPositionResponse execute(AddPositionRequest addPositionRequest) {

        DraftBeerData draftBeerData = (DraftBeerData) addPositionRequest.getBeerInfo();
        validatorService.validate(draftBeerValidator, draftBeerData);

        return AddPositionResponse.builder()
                .name(addPositionRequest.getName())
                .beerType(addPositionRequest.getBeerType())
                .alcoholPercentage(addPositionRequest.getAlcoholPercentage())
                .bitterness(addPositionRequest.getBitterness())
                .containerType(addPositionRequest.getContainerType())
                .beerInfo(new DraftBeerData(draftBeerData.getAvailableLiters()))
                .build();
    }
}
