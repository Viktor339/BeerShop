package com.shop.service.validator;

import com.shop.service.Response;
import com.shop.service.exception.IncorrectEmailException;
import com.shop.service.exception.MailNotFoundException;
import com.shop.servlet.request.RegistrationRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements Validator<RegistrationRequest> {

    public boolean mailIsValid(String s) {

        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }

    @Override
    public Response getMessage(RegistrationRequest value) {

        String mail = value.getEmail();

        Response response = new Response();
        if (mail == null) {
            throw new MailNotFoundException("Please enter you email", HttpServletResponse.SC_BAD_REQUEST);
        }
        if (mailIsValid(mail)) {
            response.setCode(HttpServletResponse.SC_OK);
        } else {
            throw new IncorrectEmailException("Incorrect email", HttpServletResponse.SC_BAD_REQUEST);
        }
        return response;
    }
}
