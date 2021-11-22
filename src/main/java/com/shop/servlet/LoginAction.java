package com.shop.servlet;

import com.shop.servlet.request.LoginRequest;
import com.shop.servlet.request.Request;

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
    public void doAction(HttpServletRequest req, HttpServletResponse resp, Request request) {

        LoginServlet loginServlet = new LoginServlet((LoginRequest) request);
        try {
            loginServlet.doPost(req,resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
