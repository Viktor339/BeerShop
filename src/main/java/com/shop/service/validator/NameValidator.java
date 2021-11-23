package com.shop.service.validator;

import com.shop.service.Response;
import com.shop.service.exception.IncorrectEmailException;
import com.shop.service.exception.NameNotFoundException;
import com.shop.servlet.request.RegistrationRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameValidator implements Validator<RegistrationRequest> {

    public boolean nameIsContainsOnlyLatinCharacters(String s) {

        String regex = "^[A-Za-z]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }

    @Override
    public Response getMessage(RegistrationRequest value) {
        String name = value.getName();

        Response response = new Response();
        if (name == null) {
            throw new NameNotFoundException("Please enter you name", HttpServletResponse.SC_BAD_REQUEST);
        }
        if (nameIsContainsOnlyLatinCharacters(name)) {
            response.setCode(HttpServletResponse.SC_OK);
        } else {
            throw new IncorrectEmailException("Incorrect name",HttpServletResponse.SC_BAD_REQUEST);
        }
        return response;
    }
}
