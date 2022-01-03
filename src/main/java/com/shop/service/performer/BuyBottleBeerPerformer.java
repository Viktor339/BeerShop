package com.shop.service.performer;

import com.shop.model.BottleBeerData;
import com.shop.model.BottleBuyBeerQuantity;
import com.shop.model.BuyBottleBeerData;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.service.exception.AvailableQuantityExceededException;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.servlet.dto.BuyPositionDto;
import com.shop.servlet.request.BuyPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BuyBottleBeerPerformer implements Performer<BuyPositionRequest, List<BuyPositionDto>> {
    private final PositionRepository positionRepository;

    @Override
    public boolean isValid(Object value) {
        return ((BuyPositionRequest) value).getBottle() != null;
    }

    @Override
    public List<BuyPositionDto> perform(BuyPositionRequest value) {

        List<BuyPositionDto> buyPositionDtoList = new ArrayList<>();

        for (BuyBottleBeerData bottleBeer : value.getBottle()) {

            Position position = positionRepository.findPositionById(bottleBeer.getId(), BottleBeerData.class)
                    .orElseThrow(() -> new PositionNotFoundException("Position not found"));

            BottleBeerData bottleBeerData = (BottleBeerData) position.getBeerInfo();
            Integer quantity = bottleBeerData.getQuantity();

            if (quantity < bottleBeer.getQuantity()) {
                throw new AvailableQuantityExceededException("The maximum available beer quantity has been exceeded");
            }

            bottleBeerData.setQuantity(quantity - bottleBeer.getQuantity());
            position.setBeerInfo(bottleBeerData);

            BuyPositionDto buyPositionDto = BuyPositionDto.builder()
                    .position(position)
                    .quantity(new BottleBuyBeerQuantity(bottleBeer.getQuantity()))
                    .quantityType("BottleBuyBeerQuantity")
                    .build();

            buyPositionDtoList.add(buyPositionDto);
        }

        return buyPositionDtoList;
    }
}
