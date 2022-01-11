package com.shop.service;

import com.shop.config.Config;
import com.shop.model.Position;
import com.shop.model.User;
import com.shop.model.UserTransaction;
import com.shop.repository.TransactionalHandler;
import com.shop.repository.UserTransactionRepository;
import com.shop.servlet.dto.GetAllUsersHistoryDto;
import com.shop.servlet.dto.GetAllUsersHistoryResponse;
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

public class GetAllUsersHistoryServiceTest {

    private final UserTransactionRepository userTransactionRepository = mock(UserTransactionRepository.class);
    private final Config config = mock(Config.class);
    private final PageService pageService = mock(PageService.class);
    private GetAllUsersHistoryService getAllUsersHistoryService;
    private List<UserTransaction> userTransactionList;
    private final TransactionalHandler transactionalHandler = new TransactionalHandler();
    private GetAllUsersHistoryResponse getAllUsersHistoryResponse;

    private Integer page;
    private Integer pageSize;
    private Integer validatedPage;

    @BeforeEach
    public void setUp() {

        getAllUsersHistoryService = new GetAllUsersHistoryService(userTransactionRepository, config, pageService,transactionalHandler);

        userTransactionList = List.of(UserTransaction.builder()
                .user(User.builder().build())
                .position(Position.builder().build())
                .build());

        List<GetAllUsersHistoryDto> userHistoryDtoList = List.of(GetAllUsersHistoryDto.builder()
                .userId(userTransactionList.get(0).getUser().getId())
                .positionId(userTransactionList.get(0).getPosition().getId())
                .build());

        getAllUsersHistoryResponse = new GetAllUsersHistoryResponse(userHistoryDtoList);

        page = 2;
        pageSize = 2;
        validatedPage = 3;

    }


    @Test
    void testGet() {

        transactionalHandler.doTransaction(session -> {

            doNothing().when(pageService).validatePage(page);

            when(pageService.getSize(pageSize, config.getMinUsersHistoryPurchasePageSize(), config.getMaxUsersHistoryPurchasePageSize())).thenReturn(validatedPage);
            when(userTransactionRepository.getAllTransactions(validatedPage, page,session)).thenReturn(userTransactionList);

            assertEquals(getAllUsersHistoryResponse, getAllUsersHistoryService.get(pageSize, page));
            verify(pageService, times(1)).validatePage(any());
            verify(pageService, times(1)).getSize(any(), any(), any());

            transactionalHandler.beginTransaction();
        });
    }
}
