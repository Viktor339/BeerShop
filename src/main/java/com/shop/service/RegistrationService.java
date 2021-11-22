package com.shop.service;

import com.shop.model.User;
import com.shop.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;

public class RegistrationService {

    public User saveUser(String name, String password, String email, HttpServletRequest req) {

        UserRepository userRepository = new UserRepository(req.getServletContext());

        return userRepository.saveUser(name, password, email);
    }
}
