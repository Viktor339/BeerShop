package com.shop.service;

import com.shop.model.User;
import com.shop.repository.TransactionalHandler;
import com.shop.repository.UserRepository;
import com.shop.service.exception.UserNotFoundException;
import com.shop.servlet.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final ValidatorService validatorService = Mockito.mock(ValidatorService.class);
    private LoginRequest loginRequest;
    private LoginService loginService;
    private final TransactionalHandler transactionalHandler = new TransactionalHandler();

    @BeforeEach
    public void setUp() {
        loginRequest = new LoginRequest();
        loginService = new LoginService(userRepository, new ArrayList<>(), validatorService, transactionalHandler);

        loginRequest.setName("name");
        loginRequest.setPassword("123");
    }


    @Test
    void testLoginShouldThrowUserNotFoundException() {

        assertThrows(UserNotFoundException.class, () -> loginService.login(loginRequest));

    }

    @Test
    void testLogin() {

        transactionalHandler.doTransaction(session -> {

            when(userRepository.getUserByNameAndPassword(loginRequest.getName(), loginRequest.getPassword(), session)).thenReturn(new User());

            assertEquals(new User(), userRepository.getUserByNameAndPassword(loginRequest.getName(), loginRequest.getPassword(), session));
            verify(userRepository, times(1)).getUserByNameAndPassword("name", "123", session);

            transactionalHandler.beginTransaction();
        });
    }
}
