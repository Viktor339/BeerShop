package com.shop.service;

import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidatorService implements Validator {
    @Override
    public ResponseMessage validate(String value) {

        ResponseMessage responseMessage = null;

        if (!mailIsValid(value)) {
            String message = "Email is not valid";
            responseMessage = new ResponseMessage(message, HttpServletResponse.SC_BAD_REQUEST);
        }

        return responseMessage;
    }


    public boolean mailIsValid(String s) {

        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }
}
