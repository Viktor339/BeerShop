package com.shop.service;

import com.shop.model.User;
import com.shop.repository.UserRepository;
import com.shop.service.exception.UserAlreadyExistsException;
import com.shop.service.exception.ValidatorException;
import com.shop.service.validator.EmailValidator;
import com.shop.service.validator.NameValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.RegistrationRequest;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RegistrationService {
    private final UserRepository userRepository;
    private final List<Validator<RegistrationRequest>> validators;

    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
        validators = Arrays.asList(
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
                .map(Validator::getMessage);

        if (invalidMessage.isPresent()) {
            throw new ValidatorException(invalidMessage.get().toString(), HttpServletResponse.SC_BAD_REQUEST);
        }

        User user = userRepository.getUserByNameOrEmail(name, email);

        if (user != null) {
            throw new UserAlreadyExistsException("Username or email already exists", HttpServletResponse.SC_BAD_REQUEST);
        }
        String UUID = DigestUtils.md5Hex(name);
        userRepository.saveUser(name, password, email, UUID,"user");

        return UUID;
    }
}
