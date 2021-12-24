package com.shop.service;

import com.shop.repository.UserRepository;
import com.shop.service.exception.UserAlreadyExistsException;
import com.shop.service.exception.ValidatorException;
import com.shop.servlet.request.RegistrationRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegistrationServiceTest {


    private final UserRepository userRepository = mock(UserRepository.class);
    private RegistrationRequest registrationRequest;
    private RegistrationService registrationService;
    private String uuid;

    @BeforeEach
    public void setUp() {
        registrationRequest = new RegistrationRequest();
        registrationService = new RegistrationService(userRepository);
        uuid = DigestUtils.md5Hex("t");

    }


    @Test
    void testRegisterShouldThrowValidatorException() {

        assertThrows(ValidatorException.class, () -> registrationService.register(registrationRequest));
    }

    @Test
    void testRegisterShouldThrowUserAlreadyExistsException() {

        registrationRequest.setPassword("t");
        registrationRequest.setEmail("t@gmail.com");
        registrationRequest.setName("t");

        when(userRepository.existsUserByNameOrEmail(registrationRequest.getName(), registrationRequest.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> registrationService.register(registrationRequest));
    }

    @Test
    void testRegister() {

        registrationRequest.setPassword("t");
        registrationRequest.setEmail("t@gmail.com");
        registrationRequest.setName("t");

        when(userRepository.existsUserByNameOrEmail(registrationRequest.getName(), registrationRequest.getEmail())).thenReturn(false);

        assertEquals(uuid, registrationService.register(registrationRequest));
        verify(userRepository, times(1)).existsUserByNameOrEmail(any(), any());
        verify(userRepository, times(1)).saveUser(any(), any(), any(), any(), any());


    }
}

