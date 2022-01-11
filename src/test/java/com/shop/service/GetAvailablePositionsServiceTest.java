package com.shop.service;

import com.shop.config.Config;
import com.shop.model.Position;
import com.shop.repository.PositionRepository;
import com.shop.repository.TransactionalHandler;
import com.shop.servlet.dto.GetAvailablePositionsDto;
import com.shop.servlet.dto.GetAvailablePositionsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetAvailablePositionsServiceTest {

    private final PositionRepository positionRepository = mock(PositionRepository.class);
    private final Config config = mock(Config.class);
    private final PageService pageService = mock(PageService.class);
    private GetAvailablePositionsService getAvailablePositionsService;
    private GetAvailablePositionsResponse getAvailablePositionsResponse;
    private final TransactionalHandler transactionalHandler = new TransactionalHandler();

    private List<Position> positionList;
    private Integer pageSize;
    private Integer page;
    private Integer validatedPage;


    @BeforeEach
    public void setUp() {

        getAvailablePositionsService = new GetAvailablePositionsService(positionRepository, config, pageService,transactionalHandler);

        Position position = Position.builder()
                .id(1)
                .build();
        positionList = List.of(position);

        List<GetAvailablePositionsDto> availablePositionsDtoList = List.of(GetAvailablePositionsDto.builder()
                .positionId(positionList.get(0).getId())
                .build());
        getAvailablePositionsResponse = new GetAvailablePositionsResponse(availablePositionsDtoList);

        page = 2;
        pageSize = 2;
        validatedPage = 2;
    }


    @Test
    void testGet() {

        transactionalHandler.doTransaction(session -> {

            doNothing().when(pageService).validatePage(page);

            when(pageService.getSize(pageSize, config.getMinUsersHistoryPurchasePageSize(), config.getMaxUsersHistoryPurchasePageSize())).thenReturn(validatedPage);
            when(positionRepository.getPositionByBeerInfo(validatedPage, page,session)).thenReturn(positionList);

            assertEquals(getAvailablePositionsResponse, getAvailablePositionsService.get(pageSize, page));
            verify(pageService, times(1)).validatePage(any());
            verify(pageService, times(1)).getSize(any(), any(), any());

            transactionalHandler.beginTransaction();

        });
    }
}
