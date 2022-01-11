package com.shop.service.performer;

import com.shop.model.BeerInfo;
import com.shop.model.BuyBeerQuantity;
import com.shop.model.BuyDraftBeerData;
import com.shop.model.DraftBeerData;
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
public class BuyDraftBeerPerformer implements Performer<BuyPositionRequest, List<BuyPositionDto>> {
    private final PositionRepository positionRepository;
    private TransactionalHandler transactionalHandler;

    @Override
    public boolean isValid(Object value) {
        return ((BuyPositionRequest) value).getDraft() != null;
    }

    @Override
    public List<BuyPositionDto> perform(BuyPositionRequest value) {

        List<BuyPositionDto> buyPositionDtoList = new ArrayList<>();

        for (BuyDraftBeerData draftBeer : value.getDraft()) {

            AtomicReference<PositionDto> positionDto = new AtomicReference<>();

            Session session = transactionalHandler.getCurrentSession();
            positionDto.set(positionRepository.findPositionById(draftBeer.getId(), session)
                    .orElseThrow(() -> new PositionNotFoundException("Position not found")));

            BeerInfo draftBeerData = positionDto.get().getBeerInfo();
            Double quantity = draftBeerData.getAvailableLiters();

            if (quantity < draftBeer.getQuantity()) {
                throw new AvailableQuantityExceededException("The maximum available beer quantity has been exceeded");
            }

            draftBeerData.setAvailableLiters(quantity - draftBeer.getQuantity());
            positionDto.get().setBeerInfo(draftBeerData);

            BuyPositionDto buyPositionDto = BuyPositionDto.builder()
                    .positionDto(positionDto.get())
                    .quantity(new BuyBeerQuantity((draftBeer.getQuantity())))
                    .quantityType("DraftBuyBeerQuantity")
                    .build();
            buyPositionDtoList.add(buyPositionDto);
        }

        return buyPositionDtoList;
    }
}
