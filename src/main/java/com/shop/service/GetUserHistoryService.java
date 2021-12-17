package com.shop.service;

import com.shop.config.Config;
import com.shop.model.UserTransaction;
import com.shop.repository.UserRepository;
import com.shop.repository.UserTransactionRepository;
import com.shop.servlet.dto.GetUserHistoryDto;
import com.shop.servlet.dto.GetUserHistoryResponse;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Collectors;

public class GetUserHistoryService {
    private final UserTransactionRepository userTransactionRepository;
    private final Config config;
    private final UserRepository userRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneOffset.UTC);
    private final PageSizeValidatorService pageSizeValidatorService;


    public GetUserHistoryService(UserTransactionRepository userTransactionRepository, Config config, UserRepository userRepository, PageSizeValidatorService pageSizeValidatorService) {
        this.userTransactionRepository = userTransactionRepository;
        this.config = config;
        this.userRepository = userRepository;
        this.pageSizeValidatorService = pageSizeValidatorService;
    }


    public GetUserHistoryResponse get(Integer pageSize, Object uuid) {

        Integer validatedPageSize = pageSizeValidatorService.validate(pageSize,
                config.getMinUserHistoryPurchasePageSize(),
                config.getMaxUserHistoryPurchasePageSize());

        Integer id = userRepository.getUserIdByUUID(uuid);

        List<UserTransaction> userTransactionList = userTransactionRepository.getTransactionsByUserId(id, validatedPageSize);

        List<GetUserHistoryDto> userHistoryDtoList = userTransactionList.stream()
                .map(n -> new GetUserHistoryDto(n.getPositionId(), n.getQuantity(), formatter.format(n.getCreated())))
                .collect(Collectors.toList());

        return new GetUserHistoryResponse(userHistoryDtoList);
    }
}
