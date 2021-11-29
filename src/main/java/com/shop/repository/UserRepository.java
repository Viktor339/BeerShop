package com.shop.repository;

import com.shop.config.Config;
import com.shop.model.User;
import com.shop.service.exception.UnableToExecuteQueryException;
import com.shop.service.exception.UnableToGetConnectionException;
import com.shop.service.exception.UserNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    private static final String SELECT_USER_BY_NAME_AND_PASSWORD = "select * from users where name=? and password=?";
    private static final String SAVE_USER = "insert into users (name,password,email,uuid,role) values (?,?,?,?,?)";
    private static final String SAVE_USER_BY_NAME_OR_EMAIL = "select * from users where name =? or email= ?";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String ROLE = "role";
    private static final String UUID = "uuid";
    private final Config config;

    public UserRepository() {
        config = new Config();
    }

    protected Connection getConnection() {
        try {
            return DataSource.getInstance(config).getConnection();
        } catch (SQLException e) {
            throw new UnableToGetConnectionException(e.getMessage());
        }
    }

    public boolean getUserByNameOrEmail(String name, String email) {

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER_BY_NAME_OR_EMAIL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);

            ResultSet rs = preparedStatement.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }


    public void saveUser(String name, String password, String email, String uuid, String role) {

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, uuid);
            preparedStatement.setString(5, role);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }


    public User getUserByNameAndPassword(String name, String password) {

        try {
            User user = null;

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_NAME_AND_PASSWORD);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                user = User.builder()
                        .name(rs.getString(NAME))
                        .password(rs.getString(PASSWORD))
                        .email(rs.getString(EMAIL))
                        .role(rs.getString(ROLE))
                        .UUID(rs.getString(UUID))
                        .build();
            }
            if (user == null) {
                throw new UserNotFoundException("Incorrect username or password");
            }
            return user;

        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }
}
