package com.shop.service.performer;

import com.shop.model.BuyDraftBeerData;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.BuyPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class BuyDraftBeerDataValidatorPerformer implements Performer<BuyPositionRequest, String> {
    private final List<Validator<BuyDraftBeerData>> buyDraftBeerDataValidator;

    @Override
    public boolean isValid(Object value) {
        BuyPositionRequest buyPositionRequest = (BuyPositionRequest) value;
        return buyPositionRequest.getDraft().size() != 0;
    }

    @Override
    public String perform(BuyPositionRequest value) {

        return value.getDraft()
                .stream()
                .map(beer -> {
                    Optional<String> message = buyDraftBeerDataValidator.stream()
                            .filter(n -> n.isValid(beer))
                            .findFirst()
                            .map(Validator::getMessage);
                    return message.orElse(null);
                }).filter(Objects::nonNull)
                .findFirst().orElse(null);
    }
}
