package com.shop.service;

import com.shop.config.Config;
import com.shop.model.UserTransaction;
import com.shop.repository.UserTransactionRepository;
import com.shop.servlet.dto.GetAllUsersHistoryDto;
import com.shop.servlet.dto.GetAllUsersHistoryResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetAllUsersHistoryServiceTest {

    private final UserTransactionRepository userTransactionRepository = mock(UserTransactionRepository.class);
    private final Config config = mock(Config.class);
    private final PageService pageService = mock(PageService.class);
    private GetAllUsersHistoryService getAllUsersHistoryService;
    private List<UserTransaction> userTransactionList;

    private GetAllUsersHistoryResponse getAllUsersHistoryResponse;

    private Integer page;
    private Integer pageSize;
    private Integer validatedPage;

    @BeforeEach
    public void setUp() {

        getAllUsersHistoryService = new GetAllUsersHistoryService(userTransactionRepository, config, pageService);

        userTransactionList = Collections.singletonList(UserTransaction.builder()
                .userId(1)
                .positionId(1)
                .build());

        List<GetAllUsersHistoryDto> userHistoryDtoList = Collections.singletonList(GetAllUsersHistoryDto.builder()
                .userId(userTransactionList.get(0).getUserId())
                .positionId(userTransactionList.get(0).getPositionId())
                .build());

        getAllUsersHistoryResponse = new GetAllUsersHistoryResponse(userHistoryDtoList);

        page = 2;
        pageSize = 2;
        validatedPage = 3;

    }


    @Test
    void testGet() {

        doNothing().when(pageService).validatePage(page);

        when(pageService.getSize(pageSize, config.getMinUsersHistoryPurchasePageSize(), config.getMaxUsersHistoryPurchasePageSize())).thenReturn(validatedPage);
        when(userTransactionRepository.getAllTransactions(validatedPage, page)).thenReturn(userTransactionList);

        assertEquals(getAllUsersHistoryResponse, getAllUsersHistoryService.get(pageSize, page));
        verify(pageService, times(1)).validatePage(any());
        verify(pageService, times(1)).getSize(any(), any(),any());
        verify(userTransactionRepository, times(1)).getAllTransactions(any(), any());

    }
}
