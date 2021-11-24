package com.shop.servlet.action;

import com.shop.model.User;
import com.shop.service.LoginService;
import com.shop.service.RequestBuilderService;
import com.shop.service.Response;
import com.shop.service.exception.IncorrectUsernameOrPasswordException;
import com.shop.service.message.InfoMessage;
import com.shop.servlet.request.LoginRequest;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class LoginAction implements Action {
    private final LoginService loginService;

    @Override
    public boolean isValid(HttpServletRequest req) {

        String path = req.getRequestURI().substring(req.getContextPath().length());
        return path.equals("/login");
    }

    @Override
    public void doAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        LoginRequest loginRequest = new RequestBuilderService().build(req, LoginRequest.class);

        try {
            User user = loginService.login(loginRequest);

            if (user.getRole().equals("user")) {
                req.getSession().setAttribute("role", "user");
                new Response().send(resp, new InfoMessage().build("You have successfully logged in like user"), HttpServletResponse.SC_OK);
            }

            if (user.getRole().equals("admin")) {
                req.getSession().setAttribute("role", "admin");
                new Response().send(resp, new InfoMessage().build("You have successfully logged in like admin"), HttpServletResponse.SC_OK);
            }

        } catch (IncorrectUsernameOrPasswordException e) {
            new Response().send(resp, new InfoMessage().build(e.getMessage()), e.getErrorCode());
        }
    }
}
