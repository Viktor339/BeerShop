package com.shop.service.validator;

import com.shop.servlet.request.RegistrationRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameValidator implements Validator<RegistrationRequest> {
    private String name;

    @Override
    public boolean isValid(RegistrationRequest value) {

        if (value.getName() == null) {
            return true;
        }
        String regex = "^[A-Za-z]";
        Pattern pattern = Pattern.compile(regex);
        name = value.getName();
        Matcher matcher = pattern.matcher(name);

        return !matcher.matches();
    }


    @Override
    public String getMessage() {
        if (name == null) {
            return "Parameter name not found";
        } else {
            return "Incorrect name";
        }
    }
}
