package com.shop.service.validator;

import com.shop.servlet.request.RegistrationRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements Validator<RegistrationRequest> {

    private String email;

    @Override
    public boolean isValid(RegistrationRequest value) {

        if (value.getEmail() == null) {
            return true;
        }
        email = value.getEmail();

        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return !matcher.matches();
    }

    @Override
    public String getMessage() {
        if (email == null) {
            return "Parameter email not found";
        } else {
            return "Incorrect email";
        }
    }
}
