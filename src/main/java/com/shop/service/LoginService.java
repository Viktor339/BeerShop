package com.shop.service;

import com.shop.model.User;
import com.shop.repository.UserRepository;
import com.shop.service.exception.UserNotFoundException;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.LoginRequest;

import java.util.List;

public class LoginService {
    private final UserRepository userRepository;
    private final List<Validator<LoginRequest>> validators;
    private final ValidatorService validatorService;

    public LoginService(UserRepository userRepository, List<Validator<LoginRequest>> validators, ValidatorService validatorService) {
        this.userRepository = userRepository;
        this.validators = validators;
        this.validatorService = validatorService;
    }

    public User login(LoginRequest loginRequest) {

        validatorService.validate(validators, loginRequest);

        return userRepository.getUserByNameAndPassword(loginRequest.getName(), loginRequest.getPassword())
                .orElseThrow(() -> new UserNotFoundException("Incorrect username or password"));
    }
}
