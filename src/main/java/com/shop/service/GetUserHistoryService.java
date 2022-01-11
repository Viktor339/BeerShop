package com.shop.service;

import com.shop.config.Config;
import com.shop.model.UserTransaction;
import com.shop.repository.TransactionalHandler;
import com.shop.repository.UserRepository;
import com.shop.repository.UserTransactionRepository;
import com.shop.servlet.dto.GetUserHistoryDto;
import com.shop.servlet.dto.GetUserHistoryResponse;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class GetUserHistoryService {
    private final UserTransactionRepository userTransactionRepository;
    private final Config config;
    private final UserRepository userRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneOffset.UTC);
    private final PageService pageService;
    private final TransactionalHandler transactionalHandler;


    public GetUserHistoryService(UserTransactionRepository userTransactionRepository, Config config, UserRepository userRepository,
                                 PageService pageService,
                                 TransactionalHandler transactionalHandler) {
        this.userTransactionRepository = userTransactionRepository;
        this.config = config;
        this.userRepository = userRepository;
        this.pageService = pageService;
        this.transactionalHandler = transactionalHandler;
    }


    public GetUserHistoryResponse get(Integer pageSize, Integer page, Object uuid) {

        pageService.validatePage(page);

        Integer validatedPageSize = pageService.getSize(pageSize,
                config.getMinUserHistoryPurchasePageSize(),
                config.getMaxUserHistoryPurchasePageSize());

        AtomicReference<List<GetUserHistoryDto>> userHistoryDtoList = new AtomicReference<>();

        transactionalHandler.doTransaction(session -> {

            Integer id = userRepository.getUserIdByUUID(uuid, session);
            List<UserTransaction> userTransactionList = userTransactionRepository.getTransactionsByUserId(id, validatedPageSize, page, session);

            userHistoryDtoList.set(userTransactionList.stream()
                    .map(n -> new GetUserHistoryDto(n.getPosition().getId(),
                            n.getQuantity(), formatter.format(n.getCreated())))
                    .collect(Collectors.toList()));
        });

        return new GetUserHistoryResponse(userHistoryDtoList.get());
    }
}
