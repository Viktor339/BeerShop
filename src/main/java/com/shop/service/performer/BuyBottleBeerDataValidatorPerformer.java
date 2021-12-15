package com.shop.service.performer;

import com.shop.model.BuyBottleBeerData;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.BuyPositionRequest;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class BuyBottleBeerDataValidatorPerformer implements Performer<BuyPositionRequest, String> {
    private final List<Validator<BuyBottleBeerData>> buyBottleBeerDataValidator;

    @Override
    public boolean isValid(Object value) {
        BuyPositionRequest buyPositionRequest = (BuyPositionRequest) value;
        return buyPositionRequest.getBottle().size() != 0;
    }

    @Override
    public String perform(BuyPositionRequest value) {

        return value.getBottle()
                .stream()
                .map(beer -> {
                    Optional<String> message = buyBottleBeerDataValidator.stream()
                            .filter(n -> n.isValid(beer))
                            .findFirst()
                            .map(Validator::getMessage);
                    return message.orElse(null);
                }).filter(Objects::nonNull)
                .findFirst().orElse(null);
    }
}
