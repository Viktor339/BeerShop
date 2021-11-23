package com.shop.service;

import com.shop.model.User;
import com.shop.repository.UserRepository;
import com.shop.service.message.InfoMessage;
import com.shop.service.message.Message;
import com.shop.servlet.request.LoginRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class LoginService {

    private final LoginRequest loginRequest;

    public LoginService(LoginRequest loginRequest) {
        super();
        this.loginRequest = loginRequest;

    }

    public void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String name = loginRequest.getName();
        String password = loginRequest.getPassword();

        UserRepository userRepository = new UserRepository(req.getServletContext());
        User user = null;

        try {
            user = userRepository.getUserByNameAndPassword(name, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Response response = new Response();
        Message infoMessage = new InfoMessage();

        if (user != null) {
            if (user.getRole() == null) {
                response.send(resp, infoMessage.build("Role not found for this user"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            if (user.getRole().equals("user")) {
                req.getSession().setAttribute("role", "user");
                response.send(resp, infoMessage.build("You have successfully logged in like user"), HttpServletResponse.SC_OK);
            }

            if (user.getRole().equals("admin")) {
                req.getSession().setAttribute("role", "admin");
                response.send(resp, infoMessage.build("You have successfully logged in like admin"), HttpServletResponse.SC_OK);
            }

        } else {
            response.send(resp, infoMessage.build("Incorrect username or password"), HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
