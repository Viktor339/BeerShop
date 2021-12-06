package com.shop.service.performer;

import com.shop.model.BuyDraftBeerData;
import com.shop.model.BuyPositionData;
import com.shop.model.DraftBeerData;
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
public class BuyDraftBeerPerformer implements Performer<BuyPositionRequest, Map<BuyPositionTransactionDTO, Position>> {
    private static final String CONTAINER_TYPE = "draft";
    private final ValidatorService validatorService;
    private final PositionRepository positionRepository;
    private final List<Validator<BuyDraftBeerData>> draftBeerValidator;

    @Override
    public boolean isValid(Object value) {
        return ((BuyPositionData) value).getBuyDraftBeerData() != null;
    }

    @Override
    public Map<BuyPositionTransactionDTO, Position> perform(BuyPositionRequest value) {

        Map<BuyPositionTransactionDTO, Position> positionIdByPosition = new HashMap<>();

        List<BuyDraftBeerData>  s = value.getBuyPositionData().getBuyDraftBeerData();
        for (BuyDraftBeerData draftBeer : s) {

            validatorService.validate(draftBeerValidator, draftBeer);

            Position position = positionRepository.findPositionByNameAndContainerType(draftBeer.getName(), CONTAINER_TYPE, DraftBeerData.class)
                    .orElseThrow(() -> new PositionNotFoundException("Position not found"));

            DraftBeerData draftBeerData = (DraftBeerData) position.getBeerInfo();
            Integer quantity = draftBeerData.getAvailableLiters();

            if (quantity < draftBeer.getQuantity()) {
                throw new AvailableQuantityExceeded("The maximum available beer quantity has been exceeded");
            }

            draftBeerData.setAvailableLiters(quantity - draftBeer.getQuantity());

            position.setBeerInfo(draftBeerData);

            BuyPositionTransactionDTO buyPositionTransactionDTO = BuyPositionTransactionDTO.builder()
                    .name(position.getName())
                    .quantity(draftBeer.getQuantity())
                    .build();
            positionIdByPosition.put(buyPositionTransactionDTO, position);
        }

        return positionIdByPosition;
    }
}
