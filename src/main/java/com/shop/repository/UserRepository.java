package com.shop.repository;

import com.shop.config.Config;
import com.shop.model.User;

import javax.servlet.ServletContext;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    private static final String SELECT_USER_BY_NAME_AND_PASSWORD = "select * from users where name=? and password=?";
    private static final String SAVE_USER = "insert into users (name,password,email,uuid) values (?,?,?,?)";
    private static final String SAVE_USER_BY_NAME_OR_EMAIL = "select * from users where name =? or email= ?";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String ROLE = "role";
    private static final String UUID = "uuid";
    private final Config config;

    public UserRepository(ServletContext servletContext) {
        config = Config.getInstance(servletContext);
    }

    protected Connection getConnection() throws SQLException {

        Connection connection = null;
        try {
            connection = DataSource.getInstance(config).getConnection();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public User getUserByNameOrEmail(String name, String email) throws SQLException {
        User user = null;

        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER_BY_NAME_OR_EMAIL);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, email);

        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            user = User.builder()
                    .name(rs.getString(NAME))
                    .email(rs.getString(EMAIL))
                    .build();
        }
        return user;
    }


    public void saveUser(String name, String password, String email, String uuid) throws SQLException {

        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, email);
        preparedStatement.setString(4, uuid);

        preparedStatement.executeQuery();
    }


    public User getUserByNameAndPassword(String name, String password) throws SQLException {
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

        return user;
    }
}
