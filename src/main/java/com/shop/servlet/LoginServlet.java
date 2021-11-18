package com.shop.servlet;

import com.shop.dao.UserDAO;
import com.shop.model.User;
import com.shop.servlet.request.LoginRequest;
import com.shop.service.ResponseMessage;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginServlet extends HttpServlet {

    public LoginServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        InputStreamReader inputStreamReader = new InputStreamReader(req.getInputStream());
        LoginRequest loginRequest = objectMapper.readValue(inputStreamReader, LoginRequest.class);

        String name = loginRequest.getName();
        String password = loginRequest.getPassword();

        UserDAO userDAO = new UserDAO(req.getServletContext());
        User user = userDAO.getUserByNameAndPassword(name, password);

        ResponseMessage responseMessage = new ResponseMessage();

        if (user != null) {
            if(user.getRole()==null){
                responseMessage.send(resp, "Role not found for this user",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

            if (user.getRole().equals("user")) {
                req.getSession().setAttribute("role","user");

                responseMessage.send(resp, "You have successfully logged in like user",HttpServletResponse.SC_OK);
            }

            if (user.getRole().equals("admin")) {
                req.getSession().setAttribute("role","admin");

                responseMessage.send(resp, "You have successfully logged in like admin",HttpServletResponse.SC_OK);
            }

        } else {
            responseMessage.send(resp, "user not found",HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
