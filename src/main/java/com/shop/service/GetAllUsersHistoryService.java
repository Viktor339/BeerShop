package com.shop.service;

import com.shop.config.Config;
import com.shop.model.UserTransaction;
import com.shop.repository.UserTransactionRepository;
import com.shop.servlet.dto.GetAllUsersHistoryDto;
import com.shop.servlet.dto.GetAllUsersHistoryResponse;

import java.util.List;
import java.util.stream.Collectors;

public class GetAllUsersHistoryService {

    private final UserTransactionRepository userTransactionRepository;
    private final Config config;
    private final PageSizeValidatorService pageSizeValidatorService;


    public GetAllUsersHistoryService(UserTransactionRepository userTransactionRepository, Config config, PageSizeValidatorService pageSizeValidatorService) {
        this.userTransactionRepository = userTransactionRepository;
        this.config = config;
        this.pageSizeValidatorService = pageSizeValidatorService;
    }

    public GetAllUsersHistoryResponse get(Integer pageSize) {

        Integer validPageSize = pageSizeValidatorService.validate(pageSize,
                config.getMinUsersHistoryPurchasePageSize(),
                config.getMaxUsersHistoryPurchasePageSize());

        List<UserTransaction> userTransactionList = userTransactionRepository.getAllTransactions(validPageSize);

        List<GetAllUsersHistoryDto> userHistoryDtoList = userTransactionList.stream()
                .map(n -> new GetAllUsersHistoryDto(n.getUserId(), n.getPositionId()))
                .collect(Collectors.toList());

        return new GetAllUsersHistoryResponse(userHistoryDtoList);
    }

}
