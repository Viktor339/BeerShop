package com.shop.service;

import com.shop.config.Config;
import com.shop.repository.TransactionRepository;
import com.shop.service.validator.MaxPageSizeValidator;
import com.shop.service.validator.MinPageSizeValidator;
import com.shop.service.validator.NotEmptyFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.dto.GetUserHistoryResponse;
import com.shop.servlet.dto.GetUserHistoryTransactionDTO;
import com.shop.servlet.request.GetUserHistoryRequest;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class GetUserHistoryService {
    private final TransactionRepository transactionRepository;
    private final ValidatorService validatorService;
    private final List<Validator<GetUserHistoryRequest>> getUserHistoryRequestValidator;
    private final List<Validator<GetUserHistoryRequest>> pageSizeValidator;


    public GetUserHistoryService(ValidatorService validatorService, TransactionRepository transactionRepository, Config config) {
        this.transactionRepository = transactionRepository;
        this.validatorService = validatorService;

        getUserHistoryRequestValidator = Collections.singletonList(
                new NotEmptyFieldValidator<>(GetUserHistoryRequest::getQuantity, "Quantity is null or empty")
        );

        pageSizeValidator = Arrays.asList(
                new MinPageSizeValidator(config.getMinUserPageSize()),
                new MaxPageSizeValidator(config.getMaxUserPageSize())
        );
    }


    public GetUserHistoryResponse get(GetUserHistoryRequest getUserHistoryRequest, Object uuid) {

        Integer userPageSize = getUserHistoryRequest.getQuantity();

        validatorService.validate(getUserHistoryRequestValidator, getUserHistoryRequest);

        final Optional<Object> validatedPageSize = pageSizeValidator.stream()
                .filter(v -> v.isValid(getUserHistoryRequest))
                .findFirst()
                .map(Validator::getResult);

        if (validatedPageSize.isPresent()) {
            userPageSize = (Integer) validatedPageSize.get();
        }

        List<GetUserHistoryTransactionDTO> userTransactionList = transactionRepository.getTransactionsByUUID(uuid);

        if (userTransactionList.size() < userPageSize) {
            return new GetUserHistoryResponse(userTransactionList);
        }

        return new GetUserHistoryResponse(userTransactionList.subList(0, userPageSize));
    }
}
