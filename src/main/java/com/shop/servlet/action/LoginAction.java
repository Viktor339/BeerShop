package com.shop.servlet.action;

import com.shop.service.JSONParseService;
import com.shop.service.LoginService;
import com.shop.servlet.request.LoginRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginAction implements Action {
    @Override
    public boolean isValid(HttpServletRequest req) {

        String path = req.getRequestURI().substring(req.getContextPath().length());
        return path.equals("/login");
    }

    @Override
    public void doAction(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        final ServletInputStream inputStream = req.getInputStream();
        final LoginRequest loginRequest = (LoginRequest) new JSONParseService().parseFromJson(inputStream, LoginRequest.class);

        LoginService loginService = new LoginService(loginRequest);
        loginService.login(req, resp);

    }

}
