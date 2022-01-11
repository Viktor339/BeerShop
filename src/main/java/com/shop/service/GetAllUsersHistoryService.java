package com.shop.service;

import com.shop.config.Config;
import com.shop.model.UserTransaction;
import com.shop.repository.TransactionalHandler;
import com.shop.repository.UserTransactionRepository;
import com.shop.servlet.dto.GetAllUsersHistoryDto;
import com.shop.servlet.dto.GetAllUsersHistoryResponse;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class GetAllUsersHistoryService {

    private final UserTransactionRepository userTransactionRepository;
    private final Config config;
    private final PageService pageService;
    private final TransactionalHandler transactionalHandler;


    public GetAllUsersHistoryService(UserTransactionRepository userTransactionRepository, Config config, PageService pageService,TransactionalHandler transactionalHandler) {
        this.userTransactionRepository = userTransactionRepository;
        this.config = config;
        this.pageService = pageService;
        this.transactionalHandler = transactionalHandler;
    }

    public GetAllUsersHistoryResponse get(Integer pageSize, Integer page) {

        pageService.validatePage(page);

        Integer validPageSize = pageService.getSize(pageSize,
                config.getMinUsersHistoryPurchasePageSize(),
                config.getMaxUsersHistoryPurchasePageSize());

        AtomicReference<List<GetAllUsersHistoryDto>> userHistoryDtoList = new AtomicReference<>();
        transactionalHandler.doTransaction(session -> {

            List<UserTransaction> userTransactionList = userTransactionRepository.getAllTransactions(validPageSize, page,session);

             userHistoryDtoList.set(userTransactionList.stream()
                     .map(n -> new GetAllUsersHistoryDto(n.getUser().getId(), n.getPosition().getId()))
                     .collect(Collectors.toList()));

        });



        return new GetAllUsersHistoryResponse(userHistoryDtoList.get());
    }

}
