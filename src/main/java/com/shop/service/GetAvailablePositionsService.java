package com.shop.service;

import com.shop.config.Config;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.repository.TransactionalHandler;
import com.shop.servlet.dto.GetAvailablePositionsDto;
import com.shop.servlet.dto.GetAvailablePositionsResponse;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class GetAvailablePositionsService {

    private final PositionRepository positionRepository;
    private final Config config;
    private final PageService pageService;
    private final TransactionalHandler transactionalHandler;

    public GetAvailablePositionsService(PositionRepository positionRepository, Config config, PageService pageService, TransactionalHandler transactionalHandler) {
        this.positionRepository = positionRepository;
        this.config = config;
        this.pageService = pageService;
        this.transactionalHandler = transactionalHandler;

    }

    public GetAvailablePositionsResponse get(Integer pageSize, Integer page) {

        pageService.validatePage(page);

        Integer validPageSize = pageService.getSize(pageSize,
                config.getMinAvailablePositionsPageSize(),
                config.getMaxAvailablePositionsPageSize());

        AtomicReference<List<Position>> positionList = new AtomicReference<>();

        transactionalHandler.doTransaction(session ->
                positionList.set(positionRepository.getPositionByBeerInfo(validPageSize, page, session)));

        List<GetAvailablePositionsDto> availablePositionsDtoList = positionList.get().stream()
                .map(n -> new GetAvailablePositionsDto(n.getId()))
                .collect(Collectors.toList());

        return new GetAvailablePositionsResponse(availablePositionsDtoList);

    }

}
