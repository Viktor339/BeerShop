package com.shop.service.performer;

import com.shop.model.BottleBeerData;
import com.shop.model.BuyBeerData;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.service.exception.AvailableQuantityExceeded;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.servlet.request.BuyPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class BuyBottleBeerPerformer implements Performer<BuyPositionRequest, Map<Position, Double>> {
    private final PositionRepository positionRepository;

    @Override
    public boolean isValid(Object value) {
        return ((BuyPositionRequest) value).getBottle() != null;
    }

    @Override
    public Map<Position, Double> perform(BuyPositionRequest value) {

        Map<Position, Double> positionByQuantity = new HashMap<>();

        for (BuyBeerData bottleBeer : value.getBottle()) {

            Position position = positionRepository.findPositionById(bottleBeer.getId(), BottleBeerData.class)
                    .orElseThrow(() -> new PositionNotFoundException("Position not found"));

            BottleBeerData bottleBeerData = (BottleBeerData) position.getBeerInfo();
            Double quantity = bottleBeerData.getQuantity();

            if (quantity < bottleBeer.getQuantity()) {
                throw new AvailableQuantityExceeded("The maximum available beer quantity has been exceeded");
            }

            bottleBeerData.setQuantity(quantity - bottleBeer.getQuantity());
            position.setBeerInfo(bottleBeerData);

            positionByQuantity.put(position, bottleBeer.getQuantity());
        }

        return positionByQuantity;
    }
}
