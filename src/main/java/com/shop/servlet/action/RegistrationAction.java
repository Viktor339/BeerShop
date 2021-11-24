package com.shop.servlet.action;

import com.shop.service.RegistrationService;
import com.shop.service.RequestBuilderService;
import com.shop.service.Response;
import com.shop.service.exception.IncorrectEmailException;
import com.shop.service.exception.MailNotFoundException;
import com.shop.service.exception.NameNotFoundException;
import com.shop.service.exception.UserAlreadyExistsException;
import com.shop.service.exception.ValidatorException;
import com.shop.service.exception.ValidatorNotFoundException;
import com.shop.service.message.InfoMessage;
import com.shop.service.message.UserMessage;
import com.shop.servlet.request.RegistrationRequest;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class RegistrationAction implements Action {
    private final RegistrationService registrationService;

    @Override
    public boolean isValid(HttpServletRequest req) {

        String path = req.getRequestURI().substring(req.getContextPath().length());
        return path.equals("/registration");
    }

    @Override
    public void doAction(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RegistrationRequest registrationRequest = new RequestBuilderService().build(req, RegistrationRequest.class);

        try {
            String userUUID = registrationService.register(registrationRequest);
            new Response().send(res, new UserMessage().build(userUUID), HttpServletResponse.SC_OK);

        } catch (ValidatorNotFoundException |
                MailNotFoundException |
                IncorrectEmailException |
                NameNotFoundException |
                ValidatorException |
                UserAlreadyExistsException e) {
            new Response().send(res, new InfoMessage().build(e.getMessage()), HttpServletResponse.SC_BAD_REQUEST);

        }
    }
}
