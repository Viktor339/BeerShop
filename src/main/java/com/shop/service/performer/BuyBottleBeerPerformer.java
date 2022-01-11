package com.shop.service.performer;

import com.shop.model.BeerInfo;
import com.shop.model.BottleBeerData;
import com.shop.model.BuyBeerQuantity;
import com.shop.model.BuyBottleBeerData;
import com.shop.repository.PositionRepository;
import com.shop.repository.TransactionalHandler;
import com.shop.service.exception.AvailableQuantityExceededException;
import com.shop.service.exception.PositionNotFoundException;
import com.shop.servlet.dto.BuyPositionDto;
import com.shop.servlet.dto.PositionDto;
import com.shop.servlet.request.BuyPositionRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@AllArgsConstructor
public class BuyBottleBeerPerformer implements Performer<BuyPositionRequest, List<BuyPositionDto>> {
    private final PositionRepository positionRepository;
    private TransactionalHandler transactionalHandler;

    @Override
    public boolean isValid(Object value) {
        return ((BuyPositionRequest) value).getBottle() != null;
    }

    @Override
    public List<BuyPositionDto> perform(BuyPositionRequest value) {

        List<BuyPositionDto> buyPositionDtoList = new ArrayList<>();

        for (BuyBottleBeerData bottleBeer : value.getBottle()) {

            AtomicReference<PositionDto> positionDto = new AtomicReference<>();

            Session session = transactionalHandler.getCurrentSession();
            positionDto.set(positionRepository.findPositionById(bottleBeer.getId(), session)
                    .orElseThrow(() -> new PositionNotFoundException("Position not found")));

            BeerInfo bottleBeerData = positionDto.get().getBeerInfo();
            Integer quantity = bottleBeerData.getQuantity();

            if (quantity < bottleBeer.getQuantity()) {
                throw new AvailableQuantityExceededException("The maximum available beer quantity has been exceeded");
            }

            bottleBeerData.setQuantity(quantity - bottleBeer.getQuantity());
            positionDto.get().setBeerInfo(bottleBeerData);

            BuyPositionDto buyPositionDto = BuyPositionDto.builder()
                    .positionDto(positionDto.get())
                    .quantity(new BuyBeerQuantity(bottleBeer.getQuantity()))
                    .quantityType("BottleBuyBeerQuantity")
                    .build();

            buyPositionDtoList.add(buyPositionDto);
        }

        return buyPositionDtoList;
    }
}
