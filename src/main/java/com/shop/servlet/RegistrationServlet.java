package com.shop.servlet;

import com.shop.model.User;
import com.shop.service.EmailValidatorService;
import com.shop.service.NameValidatorService;
import com.shop.service.RegistrationService;
import com.shop.service.ResponseMessage;
import com.shop.service.ResponseSenderService;
import com.shop.service.SearchUserWithSameNameOrEmailService;
import com.shop.service.Validator;
import com.shop.servlet.request.RegistrationRequest;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegistrationServlet extends HttpServlet {
    private final RegistrationRequest registrationRequest;


    public RegistrationServlet(RegistrationRequest registrationRequest) {
        super();
        this.registrationRequest = registrationRequest;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String name = registrationRequest.getName();
        String password = registrationRequest.getPassword();
        String email = registrationRequest.getEmail();

        Validator emailValidator = new EmailValidatorService();
        ResponseMessage emailResponseMessage = emailValidator.validate(email);
        if (emailResponseMessage != null) {
            emailResponseMessage.send(resp);
        }

        Validator nameValidator = new NameValidatorService();
        ResponseMessage nameResponseMessage = nameValidator.validate(name);
        if (nameResponseMessage != null) {
            nameResponseMessage.send(resp);
        }

        SearchUserWithSameNameOrEmailService searchUserService = new SearchUserWithSameNameOrEmailService();
        if (searchUserService.search(name, email, req) != null) {
            ResponseMessage responseMessage = new ResponseMessage("Username or email already exists", HttpServletResponse.SC_BAD_REQUEST);
            responseMessage.send(resp);
        } else {
            RegistrationService registrationService = new RegistrationService();
            User user = registrationService.saveUser(name, password, email, req);

            ObjectMapper objectMapper = new ObjectMapper();
            ResponseSenderService responseSenderService = new ResponseSenderService();
            responseSenderService.send(resp, objectMapper.writeValueAsString(user));
        }
    }
}
