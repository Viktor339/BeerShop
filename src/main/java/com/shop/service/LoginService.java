package com.shop.service;

import com.shop.model.User;
import com.shop.repository.UserRepository;
import com.shop.service.exception.IncorrectUsernameOrPasswordException;
import com.shop.servlet.request.LoginRequest;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;

    public User login(LoginRequest loginRequest) {

        User user = userRepository.getUserByNameAndPassword(loginRequest.getName(), loginRequest.getPassword());

        if (user == null) {
            throw new IncorrectUsernameOrPasswordException("Incorrect username or password", HttpServletResponse.SC_BAD_REQUEST);
        }
        return user;
    }
}
