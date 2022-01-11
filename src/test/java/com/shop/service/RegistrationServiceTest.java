package com.shop.service;

import com.shop.repository.TransactionalHandler;
import com.shop.repository.UserRepository;
import com.shop.service.exception.UserAlreadyExistsException;
import com.shop.servlet.request.RegistrationRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegistrationServiceTest {


    private final UserRepository userRepository = mock(UserRepository.class);
    private final ValidatorService validatorService = mock(ValidatorService.class);
    private RegistrationRequest registrationRequest;
    private RegistrationService registrationService;
    private String uuid;
    private final TransactionalHandler transactionalHandler = new TransactionalHandler();

    @BeforeEach
    public void setUp() {
        registrationRequest = new RegistrationRequest();
        registrationService = new RegistrationService(userRepository, new ArrayList<>(), validatorService, transactionalHandler);
        uuid = DigestUtils.md5Hex("t");

        registrationRequest.setPassword("t");
        registrationRequest.setEmail("t@gmail.com");
        registrationRequest.setName("t");

    }


    @Test
    void testRegisterShouldThrowUserAlreadyExistsException() {

        assertThrows(UserAlreadyExistsException.class,
                () -> transactionalHandler.doTransaction(session -> {

                    when(userRepository.existsUserByNameOrEmail(registrationRequest.getName(), registrationRequest.getEmail(), session)).thenReturn(true);
                    registrationService.register(registrationRequest);
                }));
    }

    @Test
    void testRegister() {

        transactionalHandler.doTransaction(session -> {

            when(userRepository.existsUserByNameOrEmail(registrationRequest.getName(), registrationRequest.getEmail(), session)).thenReturn(false);

            assertEquals(uuid, registrationService.register(registrationRequest));
            verify(userRepository, times(1)).existsUserByNameOrEmail("t", "t@gmail.com", session);

            transactionalHandler.beginTransaction();
        });
    }
}

