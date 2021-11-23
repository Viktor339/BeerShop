package com.shop.servlet.action;

import com.shop.service.JSONParseService;
import com.shop.service.RegistrationService;
import com.shop.servlet.request.RegistrationRequest;

import javax.servlet.ServletInputStream;
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
    public void doAction(HttpServletRequest req, HttpServletResponse res) throws IOException {
        final ServletInputStream inputStream = req.getInputStream();
        final RegistrationRequest registrationRequest = (RegistrationRequest) new JSONParseService().parseFromJson(inputStream, RegistrationRequest.class);

        RegistrationService registrationService = new RegistrationService(registrationRequest);
        registrationService.register(req, res);

    }
}
