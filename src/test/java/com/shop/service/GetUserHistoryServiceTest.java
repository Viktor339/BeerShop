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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserHistoryServiceTest {

    private GetUserHistoryService getUserHistoryService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneOffset.UTC);

    @Mock
    private UserTransactionRepository userTransactionRepository;
    @Mock
    private Config config;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PageService pageService;
    private final Object uuid = 5;

    private List<UserTransaction> userTransactionList;
    private GetUserHistoryResponse getUserHistoryResponse;


    @BeforeEach
    public void setUp() {
        getUserHistoryService = new GetUserHistoryService(userTransactionRepository, config, userRepository, pageService);

        userTransactionList = Collections.singletonList(UserTransaction.builder()
                .positionId(1)
                .quantity(new BottleBuyBeerQuantity(1))
                .created(Instant.now())
                .build());

        List<GetUserHistoryDto> userHistoryDtoList = userTransactionList.stream()
                .map(n -> new GetUserHistoryDto(n.getPositionId(), n.getQuantity(), formatter.format(n.getCreated())))
                .collect(Collectors.toList());

        getUserHistoryResponse = new GetUserHistoryResponse(userHistoryDtoList);

    }

    @Test
    void testGet_PageSize5Page5UUID5_ShouldReturnGetUserHistoryResponse() {

        Integer page = 5;
        Integer validatedPageSize = 5;
        Integer id = 4;
        Integer pageSize = 5;

        doNothing().when(pageService).validatePage(page);
        when(pageService.getSize(any(), any(), any())).thenReturn(validatedPageSize);
        when(userRepository.getUserIdByUUID(any())).thenReturn(id);
        when(userTransactionRepository.getTransactionsByUserId(id, validatedPageSize, page)).thenReturn(userTransactionList);

        assertEquals(getUserHistoryResponse, getUserHistoryService.get(pageSize, page, uuid));
    }
}

