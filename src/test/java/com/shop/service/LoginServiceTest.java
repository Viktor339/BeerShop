package com.shop.service;

import com.shop.model.User;
import com.shop.repository.UserRepository;
import com.shop.service.exception.UserNotFoundException;
import com.shop.service.exception.ValidatorException;
import com.shop.servlet.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private LoginRequest loginRequest;
    private LoginService loginService;
    private Optional<User> optionalUser;

    @BeforeEach
    public void setUp() {
        loginRequest = new LoginRequest();
        loginService = new LoginService(userRepository);
        optionalUser = Optional.of(User.builder().build());

        loginRequest.setName("name");
        loginRequest.setPassword("123");
    }


    @Test
    void testLoginShouldThrowValidatorException() {
        loginRequest.setName("");

        assertThrows(ValidatorException.class, () -> loginService.login(loginRequest));

    }

    @Test
    void testLoginShouldThrowUserNotFoundException() {

        assertThrows(UserNotFoundException.class, () -> loginService.login(loginRequest));

    }

    @Test
    void testLogin() {

        when(userRepository.getUserByNameAndPassword(loginRequest.getName(), loginRequest.getPassword())).thenReturn(Optional.ofNullable(User.builder().build()));

        assertEquals(optionalUser, userRepository.getUserByNameAndPassword(loginRequest.getName(), loginRequest.getPassword()));
        verify(userRepository, times(1)).getUserByNameAndPassword(any(),any());

    }
}
