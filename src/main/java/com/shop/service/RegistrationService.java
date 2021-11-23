package com.shop.service;

import com.shop.model.User;
import com.shop.repository.UserRepository;
import com.shop.service.exception.IncorrectEmailException;
import com.shop.service.exception.MailNotFoundException;
import com.shop.service.exception.NameNotFoundException;
import com.shop.service.message.InfoMessage;
import com.shop.service.message.Message;
import com.shop.service.message.UserMessage;
import com.shop.service.validator.EmailValidator;
import com.shop.service.validator.NameValidator;
import com.shop.service.validator.Validator;
import com.shop.servlet.request.RegistrationRequest;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class RegistrationService {

    private final RegistrationRequest registrationRequest;
    List<Validator<RegistrationRequest>> validators;

    public RegistrationService(RegistrationRequest registrationRequest) {
        this.registrationRequest = registrationRequest;
        validators = Arrays.asList(
                new EmailValidator(),
                new NameValidator()
        );
    }

    public void register(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = registrationRequest.getName();
        String password = registrationRequest.getPassword();
        String email = registrationRequest.getEmail();
        User user;
        Message infoMessage = new InfoMessage();
        Response response = new Response();

        try {

            for(Validator<RegistrationRequest> validator: validators){
                response=validator.getMessage(registrationRequest);
            }

            UserRepository userRepository = new UserRepository(req.getServletContext());
            user = userRepository.getUserByNameOrEmail(name, email);

            if (user != null) {
                response.send(resp, infoMessage.build("Username or email already exists"), HttpServletResponse.SC_BAD_REQUEST);
            } else {
                userRepository.saveUser(name, password, email, DigestUtils.md5Hex(name));
            }

        } catch (NoSuchElementException e) {
            response.send(resp, infoMessage.build("Validator not found"), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (MailNotFoundException e) {
            response.send(resp, infoMessage.build(e.getMessage()), e.getErrorCode());
        } catch (IncorrectEmailException e) {
            response.send(resp, infoMessage.build(e.getMessage()), e.getErrorCode());
        } catch (NameNotFoundException e) {
            response.send(resp, infoMessage.build(e.getMessage()), e.getErrorCode());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Message userMessage = new UserMessage();
        response.send(resp, userMessage.build(DigestUtils.md5Hex(name)), HttpServletResponse.SC_OK);
    }
}
