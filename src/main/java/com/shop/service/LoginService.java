package com.shop.service;

import com.shop.model.User;
import com.shop.repository.TransactionalHandler;
import com.shop.repository.UserRepository;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.LoginRequest;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LoginService {
    private final UserRepository userRepository;
    private final List<Validator<LoginRequest>> validators;
    private final ValidatorService validatorService;
    private final TransactionalHandler transactionalHandler;

    public LoginService(UserRepository userRepository, List<Validator<LoginRequest>> validators, ValidatorService validatorService, TransactionalHandler transactionalHandler) {
        this.userRepository = userRepository;
        this.validators = validators;
        this.validatorService = validatorService;
        this.transactionalHandler = transactionalHandler;
    }

    public User login(LoginRequest loginRequest) {

        validatorService.validate(validators, loginRequest);
        AtomicReference<User> user = new AtomicReference<>();

        transactionalHandler.doTransaction(session ->
                user.set(userRepository.getUserByNameAndPassword(loginRequest.getName(), loginRequest.getPassword(), session)));

        return user.get();
    }
}
