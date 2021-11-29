package com.shop.service.validator;

import com.shop.servlet.request.RegistrationRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameValidator implements Validator<RegistrationRequest> {

    @Override
    public boolean isValid(RegistrationRequest value) {

        String regex = "^[A-Za-z]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value.getName());

        return !matcher.matches();
    }


    @Override
    public String getMessage(RegistrationRequest registrationRequest) {
        return "Incorrect name " + registrationRequest.getName() + ". Name must contain only Latin characters";
    }
}
