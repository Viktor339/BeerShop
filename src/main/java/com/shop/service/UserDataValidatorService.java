package com.shop.service;

import com.shop.dao.UserDAO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserDataValidatorService {

    public boolean mailIsContainsOnlyLatinCharacters(String s) {

        String regex = "^[A-Za-z]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }

    public boolean mailIsValid(String s) {
        String regex =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                        "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);

        return matcher.matches();
    }


    public boolean validate(String name, String email, HttpServletResponse resp, HttpServletRequest req) throws IOException {

        ResponseMessage responseMessage = new ResponseMessage();

        if (!mailIsContainsOnlyLatinCharacters(name)) {
            responseMessage.send(resp, "Username must contain only Latin letters",HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }

        if (!mailIsValid(email)) {
            responseMessage.send(resp, "Email is not valid",HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }


        String searchByNameQuery = "select * from users where name=?";
        String searchByEmailQuery = "select * from users where email =?";
        UserDAO userDAO = new UserDAO(req.getServletContext());
        if (userDAO.getUserByNameOrEmail(searchByEmailQuery, email) != null &
                userDAO.getUserByNameOrEmail(searchByNameQuery, name) != null) {
            responseMessage.send(resp, "Username or email already exists",HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }
        return true;
    }
}
