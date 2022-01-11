package com.shop.service;

import com.shop.repository.TransactionalHandler;
import com.shop.repository.UserRepository;
import com.shop.service.exception.UserAlreadyExistsException;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.RegistrationRequest;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

public class RegistrationService {
    private final UserRepository userRepository;
    private final List<Validator<RegistrationRequest>> validators;
    private final ValidatorService validatorService;
    private final TransactionalHandler transactionalHandler;

    public RegistrationService(UserRepository userRepository, List<Validator<RegistrationRequest>> validators, ValidatorService validatorService, TransactionalHandler transactionalHandler
    ) {
        this.userRepository = userRepository;
        this.validators = validators;
        this.validatorService = validatorService;
        this.transactionalHandler = transactionalHandler;
    }

    public String register(RegistrationRequest registrationRequest) {

        String name = registrationRequest.getName();
        String password = registrationRequest.getPassword();
        String email = registrationRequest.getEmail();

        validatorService.validate(validators, registrationRequest);

        String UUID = DigestUtils.md5Hex(name);

        transactionalHandler.doTransaction(session -> {

            if (userRepository.existsUserByNameOrEmail(name, email,session)) {
                throw new UserAlreadyExistsException("Username or email already exists");
            }
            userRepository.saveUser(name, password, email, UUID, "user",session);

        });


        return UUID;
    }
}
