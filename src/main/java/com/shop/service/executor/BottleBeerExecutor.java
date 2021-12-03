package com.shop.service.executor;

import com.shop.model.BottleBeerData;
import com.shop.service.ValidatorService;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.AddPositionResponse;
import com.shop.servlet.request.AddPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BottleBeerExecutor implements Executor<AddPositionRequest, AddPositionResponse>{
    private final ValidatorService validatorService;
    private final List<Validator<BottleBeerData>> bottleBeerValidator ;

    @Override
    public boolean isValid(String value) {
        return value.equals("bottle");
    }

    @Override
    public AddPositionResponse execute(AddPositionRequest addPositionRequest) {

        BottleBeerData bottleBeerData = (BottleBeerData) addPositionRequest.getBeerInfo();
        validatorService.validate(bottleBeerValidator, bottleBeerData);

        return AddPositionResponse.builder()
                .name(addPositionRequest.getName())
                .beerType(addPositionRequest.getBeerType())
                .alcoholPercentage(addPositionRequest.getAlcoholPercentage())
                .bitterness(addPositionRequest.getBitterness())
                .containerType(addPositionRequest.getContainerType())
                .beerInfo(new BottleBeerData(bottleBeerData.getContainerVolume(),bottleBeerData.getQuantity()))
                .build();
    }
}
