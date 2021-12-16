package com.shop.service;

import com.shop.config.Config;
import com.shop.model.UserTransaction;
import com.shop.repository.UserRepository;
import com.shop.repository.UserTransactionRepository;
import com.shop.service.validator.NotEmptyFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.GetUserHistoryDto;
import com.shop.servlet.dto.GetUserHistoryResponse;
import com.shop.servlet.request.GetUserHistoryRequest;
import lombok.RequiredArgsConstructor;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetUserHistoryService {
    private final UserTransactionRepository userTransactionRepository;
    private final ValidatorService validatorService;
    private final List<Validator<GetUserHistoryRequest>> getUserHistoryRequestValidator;
    private final Config config;
    private final UserRepository userRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withZone(ZoneOffset.UTC);


    public GetUserHistoryService(ValidatorService validatorService, UserTransactionRepository userTransactionRepository, Config config, UserRepository userRepository) {
        this.userTransactionRepository = userTransactionRepository;
        this.validatorService = validatorService;
        this.config = config;
        this.userRepository = userRepository;

        getUserHistoryRequestValidator = Collections.singletonList(
                new NotEmptyFieldValidator<>(GetUserHistoryRequest::getQuantity, "Quantity is null or empty")
        );
    }


    public GetUserHistoryResponse get(GetUserHistoryRequest getUserHistoryRequest, Object uuid) {

        validatorService.validate(getUserHistoryRequestValidator, getUserHistoryRequest);

        Integer validatedPageSize = checkPageSize(getUserHistoryRequest.getQuantity());
        Integer id = userRepository.getUserIdByUUID(uuid);

        List<UserTransaction> userTransactionList = userTransactionRepository.getTransactionsByUserId(id, validatedPageSize);

        List<GetUserHistoryDto> userHistoryDtoList = userTransactionList.stream()
                .map(n -> new GetUserHistoryDto(n.getPositionId(), n.getQuantity(), formatter.format(n.getCreated())))
                .collect(Collectors.toList());

        return new GetUserHistoryResponse(userHistoryDtoList);
    }

    private Integer checkPageSize(Integer userPageSize) {
        Integer minPageSize = config.getMinUserPageSize();
        Integer maxPageSize = config.getMaxUserPageSize();

        if (userPageSize < minPageSize) {
            return minPageSize;
        }
        if (userPageSize > maxPageSize) {
            return maxPageSize;
        }
        return userPageSize;
    }
}
