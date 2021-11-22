package com.shop.servlet;

import com.shop.servlet.request.RegistrationRequest;
import com.shop.servlet.request.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegistrationAction implements Action {

    @Override
    public boolean isValid(HttpServletRequest req) {

        String path = req.getRequestURI().substring(req.getContextPath().length());
        return path.equals("/registration");
    }

    @Override
    public void doAction(HttpServletRequest req, HttpServletResponse res, Request request) {
        RegistrationServlet registrationServlet = new RegistrationServlet((RegistrationRequest) request);
        try {
            registrationServlet.doPost(req, res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
