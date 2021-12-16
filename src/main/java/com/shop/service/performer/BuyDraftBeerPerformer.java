package com.shop.service.performer;

import com.shop.model.BuyDraftBeerData;
import com.shop.model.DraftBeerData;
import com.shop.model.DraftBuyBeerQuantity;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.service.exception.AvailableQuantityExceeded;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.servlet.dto.BuyPositionDto;
import com.shop.servlet.request.BuyPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BuyDraftBeerPerformer implements Performer<BuyPositionRequest, List<BuyPositionDto>> {
    private final PositionRepository positionRepository;

    @Override
    public boolean isValid(Object value) {
        return ((BuyPositionRequest) value).getDraft() != null;
    }

    @Override
    public List<BuyPositionDto> perform(BuyPositionRequest value) {

        List<BuyPositionDto> buyPositionDtoList = new ArrayList<>();

        for (BuyDraftBeerData draftBeer : value.getDraft()) {

            Position position = positionRepository.findPositionById(draftBeer.getId(), DraftBeerData.class)
                    .orElseThrow(() -> new PositionNotFoundException("Position not found"));

            DraftBeerData draftBeerData = (DraftBeerData) position.getBeerInfo();
            Double quantity = draftBeerData.getAvailableLiters();

            if (quantity < draftBeer.getQuantity()) {
                throw new AvailableQuantityExceeded("The maximum available beer quantity has been exceeded");
            }

            draftBeerData.setAvailableLiters(quantity - draftBeer.getQuantity());
            position.setBeerInfo(draftBeerData);

            BuyPositionDto buyPositionDto = BuyPositionDto.builder()
                    //  .userId(id)
                    .position(position)
                    .quantity(new DraftBuyBeerQuantity((draftBeer.getQuantity())))
                    .quantityType("DraftBuyBeerQuantity")
                    .build();
            buyPositionDtoList.add(buyPositionDto);
        }

        return buyPositionDtoList;
    }
}
