package com.shop.service;

import com.shop.config.Config;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.servlet.dto.GetAvailablePositionsDto;
import com.shop.servlet.dto.GetAvailablePositionsResponse;

import java.util.List;
import java.util.stream.Collectors;

public class GetAvailablePositionsService {

    private final PositionRepository positionRepository;
    private final Config config;
    private final PageService pageService;

    public GetAvailablePositionsService(PositionRepository positionRepository, Config config, PageService pageService) {
        this.positionRepository = positionRepository;
        this.config = config;
        this.pageService = pageService;

    }

    public GetAvailablePositionsResponse get(Integer pageSize,Integer page) {

        pageService.validatePage(page);

        Integer validPageSize = pageService.getSize(pageSize,
                config.getMinAvailablePositionsPageSize(),
                config.getMaxAvailablePositionsPageSize());


        List<Position> positionList = positionRepository.getPositionByBeerInfo(validPageSize,page);


        List<GetAvailablePositionsDto> availablePositionsDtoList = positionList.stream()
                .map(n -> new GetAvailablePositionsDto(n.getId()))
                .collect(Collectors.toList());

        return new GetAvailablePositionsResponse(availablePositionsDtoList);

    }

}
