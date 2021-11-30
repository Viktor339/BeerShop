package com.shop.service.validator;

import com.shop.servlet.request.RegistrationRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements Validator<RegistrationRequest> {

    @Override
    public boolean isValid(RegistrationRequest value) {

        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value.getEmail());

        return !matcher.matches();
    }

    @Override
    public String getMessage() {
        return "Incorrect email" ;
    }
}
