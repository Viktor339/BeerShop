package com.shop.servlet;

import com.shop.dao.UserDAO;
import com.shop.model.User;
import com.shop.servlet.request.RegistrationRequest;
import com.shop.service.UserDataValidatorService;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class RegistrationServlet extends HttpServlet {
    public RegistrationServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        InputStreamReader inputStreamReader = new InputStreamReader(req.getInputStream());
        RegistrationRequest registrationRequest = objectMapper.readValue(inputStreamReader, RegistrationRequest.class);

        String name = registrationRequest.getName();
        String password = registrationRequest.getPassword();
        String email = registrationRequest.getEmail();

        UserDataValidatorService userDataValidatorService = new UserDataValidatorService();
        boolean resultOfValidation = userDataValidatorService.validate(name, email, resp, req);

        if (resultOfValidation) {

            UserDAO userDAO = new UserDAO(req.getServletContext());
            User user = userDAO.saveUser(name, password, email);

            String jsonUser = objectMapper.writeValueAsString(user);

            PrintWriter out = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(jsonUser);
            out.flush();

        }
    }
}
