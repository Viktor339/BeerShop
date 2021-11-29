package com.shop.service;

import com.shop.model.User;
import com.shop.repository.UserRepository;
import com.shop.service.exception.ValidatorException;
import com.shop.service.validator.NotNullFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.LoginRequest;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class LoginService {
    private final UserRepository userRepository;
    private final List<Validator<LoginRequest>> validators;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
        validators = Collections.singletonList(
                new NotNullFieldValidator<>(
                        req -> Stream.of(req.getName(), req.getPassword())
                                .noneMatch(Objects::isNull),
                        req -> "Error. Request must contain the field: " + Stream.of(req)
                                .map(field -> field.getName() == null ? "name" : "password")
                                .findFirst()
                                .get()
                )
        );
    }

    public User login(LoginRequest loginRequest) {

        final Optional<Object> invalidMessage = validators.stream()
                .filter(v -> v.isValid(loginRequest))
                .findFirst()
                .map(v -> v.isValid(loginRequest));

        if (invalidMessage.isPresent()) {
            throw new ValidatorException(invalidMessage.get().toString());
        }

        return userRepository.getUserByNameAndPassword(loginRequest.getName(), loginRequest.getPassword());
    }
}
