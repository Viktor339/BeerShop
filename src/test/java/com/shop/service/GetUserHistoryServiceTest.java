package com.shop.service;

import com.shop.config.Config;
import com.shop.model.BottleBuyBeerQuantity;
import com.shop.model.UserTransaction;
import com.shop.repository.UserRepository;
import com.shop.repository.UserTransactionRepository;
import com.shop.servlet.dto.GetUserHistoryDto;
import com.shop.servlet.dto.GetUserHistoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetUserHistoryServiceTest {

    private GetUserHistoryService getUserHistoryService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneOffset.UTC);

    private final UserTransactionRepository userTransactionRepository = mock(UserTransactionRepository.class);
    private final Config config = mock(Config.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final PageService pageService = mock(PageService.class);
    private final Object uuid = 5;

    private List<UserTransaction> userTransactionList;
    private GetUserHistoryResponse getUserHistoryResponse;

    private Integer page;
    private Integer validatedPageSize;
    private Integer id;
    private Integer pageSize;


    @BeforeEach
    public void setUp() {
        getUserHistoryService = new GetUserHistoryService(userTransactionRepository, config, userRepository, pageService);

        userTransactionList = List.of(UserTransaction.builder()
                .positionId(1)
                .quantity(new BottleBuyBeerQuantity(1))
                .created(Instant.now())
                .build());

        List<GetUserHistoryDto> userHistoryDtoList = userTransactionList.stream()
                .map(n -> new GetUserHistoryDto(n.getPositionId(), n.getQuantity(), formatter.format(n.getCreated())))
                .collect(Collectors.toList());

        getUserHistoryResponse = new GetUserHistoryResponse(userHistoryDtoList);
        page = 5;
        validatedPageSize = 5;
        id = 4;
        pageSize = 5;

    }

    @Test
    void testGetShouldReturnGetUserHistoryResponse() {

        doNothing().when(pageService).validatePage(page);
        when(pageService.getSize(any(), any(), any())).thenReturn(validatedPageSize);
        when(userRepository.getUserIdByUUID(any())).thenReturn(id);
        when(userTransactionRepository.getTransactionsByUserId(id, validatedPageSize, page)).thenReturn(userTransactionList);

        assertEquals(getUserHistoryResponse, getUserHistoryService.get(pageSize, page, uuid));
        verify(pageService, times(1)).validatePage(any());
        verify(pageService, times(1)).getSize(any(), any(), any());
        verify(userRepository, times(1)).getUserIdByUUID(any());
        verify(userTransactionRepository, times(1)).getTransactionsByUserId(any(), any(), any());

    }
}

