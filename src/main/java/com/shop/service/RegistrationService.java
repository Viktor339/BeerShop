package com.shop.service;

import com.shop.repository.UserRepository;
import com.shop.service.exception.UserAlreadyExistsException;
import com.shop.service.exception.ValidatorException;
import com.shop.service.validator.EmailValidator;
import com.shop.service.validator.NameValidator;
import com.shop.service.validator.NotNullFieldValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.RegistrationRequest;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class RegistrationService {
    private final UserRepository userRepository;
    private final List<Validator<RegistrationRequest>> validators;

    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
        validators = Arrays.asList(
                new NotNullFieldValidator<>(
                        req -> Stream.of(req.getName(), req.getPassword(), req.getEmail())
                                .noneMatch(Objects::isNull),
                        req -> "Error. Request must contain the field: " + Stream.of(req)
                                .map(field -> field.getName() == null ? "name" : field.getEmail() == null ? "email" : "password")
                                .findFirst()
                                .get()
                ),
                new EmailValidator(),
                new NameValidator()
        );
    }

    public String register(RegistrationRequest registrationRequest) {

        String name = registrationRequest.getName();
        String password = registrationRequest.getPassword();
        String email = registrationRequest.getEmail();

        final Optional<Object> invalidMessage = validators.stream()
                .filter(v -> v.isValid(registrationRequest))
                .findFirst()
                .map(v -> v.getMessage(registrationRequest));

        if (invalidMessage.isPresent()) {
            throw new ValidatorException(invalidMessage.get().toString());
        }

        if (userRepository.getUserByNameOrEmail(name, email)) {
            throw new UserAlreadyExistsException("Username or email already exists");
        }
        String UUID = DigestUtils.md5Hex(name);
        userRepository.saveUser(name, password, email, UUID, "user");

        return UUID;
    }
}
