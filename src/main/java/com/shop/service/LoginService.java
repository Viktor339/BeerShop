package com.shop.service;

import com.shop.model.User;
import com.shop.repository.UserRepository;
import com.shop.service.exception.UserNotFoundException;
import com.shop.service.exception.ValidatorException;
import com.shop.service.validator.NotEmptyFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.LoginRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LoginService {
    private final UserRepository userRepository;
    private final List<Validator<LoginRequest>> validators;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
        validators = Arrays.asList(
                new NotEmptyFieldValidator<>(LoginRequest::getName, "Name is null"),
                new NotEmptyFieldValidator<>(LoginRequest::getPassword, "Password is null")
        );
    }

    public User login(LoginRequest loginRequest) {

        final Optional<Object> invalidMessage = validators.stream()
                .filter(v -> v.isValid(loginRequest))
                .findFirst()
                .map(Validator::getResult);

        if (invalidMessage.isPresent()) {
            throw new ValidatorException(invalidMessage.get().toString());
        }

        return userRepository.getUserByNameAndPassword(loginRequest.getName(), loginRequest.getPassword())
                .orElseThrow(() -> new UserNotFoundException("Incorrect username or password"));
    }
}
