package com.shop.service;

import com.shop.repository.UserRepository;
import com.shop.model.User;

import javax.servlet.http.HttpServletRequest;

public class SearchUserWithSameNameOrEmailService {

    public User search(String name, String email, HttpServletRequest req) {
        UserRepository userRepository = new UserRepository(req.getServletContext());
        return userRepository.getUserByNameOrEmail(name, email);
    }
}
