package com.shop.service.performer;

import com.shop.model.BeerInfo;
import com.shop.service.ValidatorService;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.AddPositionDto;
import com.shop.servlet.request.AddPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BottleBeerPerformer implements Performer<AddPositionRequest, AddPositionDto> {
    private final ValidatorService validatorService;
    private final List<Validator<BeerInfo>> bottleBeerValidator ;

    @Override
    public boolean isValid(Object value) {
        return value.equals("bottle");
    }

    @Override
    public AddPositionDto perform(AddPositionRequest addPositionRequest) {

        BeerInfo bottleBeerData =  addPositionRequest.getBeerInfo();
        validatorService.validate(bottleBeerValidator, bottleBeerData);

        return AddPositionDto.builder()
                .name(addPositionRequest.getName())
                .beerType(addPositionRequest.getBeerType())
                .alcoholPercentage(addPositionRequest.getAlcoholPercentage())
                .bitterness(addPositionRequest.getBitterness())
                .containerType(addPositionRequest.getContainerType())
                .beerInfo(new BeerInfo(bottleBeerData.getContainerVolume(),bottleBeerData.getQuantity()))
                .build();
    }
}
