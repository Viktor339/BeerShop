package com.shop.service.performer;

import com.shop.model.BuyBeerData;
import com.shop.model.DraftBeerData;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.service.exception.AvailableQuantityExceeded;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.servlet.request.BuyPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class BuyDraftBeerPerformer implements Performer<BuyPositionRequest, Map<Position, Double>> {
    private final PositionRepository positionRepository;

    @Override
    public boolean isValid(Object value) {
        return ((BuyPositionRequest) value).getDraft() != null;
    }

    @Override
    public Map<Position, Double> perform(BuyPositionRequest value) {

        Map<Position, Double> positionByQuantity = new HashMap<>();

        for (BuyBeerData draftBeer : value.getDraft()) {

            Position position = positionRepository.findPositionById(draftBeer.getId(), DraftBeerData.class)
                    .orElseThrow(() -> new PositionNotFoundException("Position not found"));

            DraftBeerData draftBeerData = (DraftBeerData) position.getBeerInfo();
            Double quantity = draftBeerData.getAvailableLiters();

            if (quantity < draftBeer.getQuantity()) {
                throw new AvailableQuantityExceeded("The maximum available beer quantity has been exceeded");
            }

            draftBeerData.setAvailableLiters(quantity - draftBeer.getQuantity());
            position.setBeerInfo(draftBeerData);

            positionByQuantity.put(position, draftBeer.getQuantity());
        }

        return positionByQuantity;
    }
}
