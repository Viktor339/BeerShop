package com.shop.service;

import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameValidatorService implements Validator {
    @Override
    public ResponseMessage validate(String value) {

        ResponseMessage responseMessage = null;
        if (!nameIsContainsOnlyLatinCharacters(value)) {
            String message = "Username must contain only Latin letters";

             responseMessage = new ResponseMessage(message, HttpServletResponse.SC_BAD_REQUEST);
        }
        return responseMessage;

    }


    public boolean nameIsContainsOnlyLatinCharacters(String s) {

        String regex = "^[A-Za-z]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }
}
