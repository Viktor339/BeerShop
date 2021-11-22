package com.shop.servlet;

import com.shop.repository.UserRepository;
import com.shop.model.User;
import com.shop.service.ResponseMessage;
import com.shop.servlet.request.LoginRequest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private final LoginRequest loginRequest;

    public LoginServlet(LoginRequest loginRequest) {
        super();
        this.loginRequest=loginRequest;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String name = loginRequest.getName();
        String password = loginRequest.getPassword();

        UserRepository userRepository = new UserRepository(req.getServletContext());
        User user = userRepository.getUserByNameAndPassword(name, password);

        ResponseMessage responseMessage = new ResponseMessage();

        if (user != null) {
            if (user.getRole() == null) {
                responseMessage.send(resp, "Role not found for this user", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            if (user.getRole().equals("user")) {
                req.getSession().setAttribute("role", "user");

                responseMessage.send(resp, "You have successfully logged in like user", HttpServletResponse.SC_OK);
            }

            if (user.getRole().equals("admin")) {
                req.getSession().setAttribute("role", "admin");

                responseMessage.send(resp, "You have successfully logged in like admin", HttpServletResponse.SC_OK);
            }

        } else {
            responseMessage.send(resp, "user not found", HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
