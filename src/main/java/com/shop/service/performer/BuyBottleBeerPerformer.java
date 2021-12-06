package com.shop.service.performer;

import com.shop.model.BottleBeerData;
import com.shop.model.BuyBottleBeerData;
import com.shop.model.BuyPositionData;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.service.ValidatorService;
import com.shop.service.exception.AvailableQuantityExceeded;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.BuyPositionTransactionDTO;
import com.shop.servlet.request.BuyPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BuyBottleBeerPerformer implements Performer<BuyPositionRequest, Map<BuyPositionTransactionDTO, Position>> {
    private static final String CONTAINER_TYPE = "bottle";
    private final ValidatorService validatorService;
    private final PositionRepository positionRepository;
    private final List<Validator<BuyBottleBeerData>> bottleBeerValidator;

    @Override
    public boolean isValid(Object value) {
        return ((BuyPositionData) value).getBuyBottleBeerData() != null;
    }

    @Override
    public Map<BuyPositionTransactionDTO, Position> perform(BuyPositionRequest value) {

        Map<BuyPositionTransactionDTO, Position> positionIdByPosition = new HashMap<>();

        for (BuyBottleBeerData bottleBeer : value.getBuyPositionData().getBuyBottleBeerData()) {

            validatorService.validate(bottleBeerValidator, bottleBeer);

            Position position = positionRepository.findPositionByNameAndContainerType(bottleBeer.getName(), CONTAINER_TYPE, BottleBeerData.class)
                    .orElseThrow(() -> new PositionNotFoundException("Position not found"));

            BottleBeerData bottleBeerData = (BottleBeerData) position.getBeerInfo();
            Integer quantity = bottleBeerData.getQuantity();

            if (quantity < bottleBeer.getQuantity()) {
                throw new AvailableQuantityExceeded("The maximum available beer quantity has been exceeded");
            }

            bottleBeerData.setQuantity(quantity - bottleBeer.getQuantity());
            position.setBeerInfo(bottleBeerData);

            BuyPositionTransactionDTO buyPositionTransactionDTO = BuyPositionTransactionDTO.builder()
                    .name(position.getName())
                    .quantity(bottleBeer.getQuantity())
                    .build();

            positionIdByPosition.put(buyPositionTransactionDTO, position);
        }

        return positionIdByPosition;
    }
}
