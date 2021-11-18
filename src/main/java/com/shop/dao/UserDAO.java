package com.shop.dao;

import com.shop.config.Config;
import com.shop.model.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.ServletContext;
import java.sql.*;

public class UserDAO {

    private static final String SELECT_USER_BY_NAME_AND_PASSWORD = "select * from users where name=? and password=?";
    private static final String SAVE_USER = "insert into users (name,password,email,uuid) values (?,?,?,?)";
    private Config config;

    public UserDAO(ServletContext servletContext) {
        config = Config.getInstance(servletContext);
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(config.getProperty("jdbc.Driver"));
            String jdbcPassword = config.getProperty("jdbc.Password");
            String jdbcURL = config.getProperty("jdbc.URL");
            String jdbcUsername = config.getProperty("jdbc.Username");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public User getUserByNameOrEmail(String query, String nameOrEmail) {
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nameOrEmail);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                user = User.builder()
                        .name(rs.getString("name"))
                        .password(rs.getString("password"))
                        .email(rs.getString("email"))
                        .build();
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }


    public User saveUser(String name, String password, String email) {
        User user = null;
        String UUID = DigestUtils.md5Hex(name);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, UUID);

            user = User.builder()
                    .name(name)
                    .password(password)
                    .email(email)
                    .UUID(UUID)
                    .build();

            preparedStatement.executeQuery();

        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }


    public User getUserByNameAndPassword(String name, String password) {
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_NAME_AND_PASSWORD)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);

            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                user = User.builder()
                        .name(rs.getString("name"))
                        .password(rs.getString("password"))
                        .email(rs.getString("email"))
                        .role(rs.getString("role"))
                        .UUID(rs.getString("uuid"))
                        .build();
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }


    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

}
