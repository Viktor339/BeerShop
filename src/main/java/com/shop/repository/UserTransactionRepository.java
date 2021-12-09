package com.shop.repository;

import com.shop.config.Config;
import com.shop.model.UserTransaction;
import com.shop.service.exception.UnableToExecuteQueryException;
import com.shop.service.exception.UnableToGetConnectionException;
import com.shop.servlet.dto.BuyPositionDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class UserTransactionRepository {
    private static final String SELECT_TRANSACTION_BY_USERID = "select * from users_transactions where user_id=?";
    private static final String SAVE_TRANSACTION =
            "insert into users_transactions (user_id,name,quantity,date) values (?,?,?,?)";
    private static final String NAME = "name";
    private static final String QUANTITY = "quantity";
    private static final String DATE = "date";

    private final Config config;


    public UserTransactionRepository(Config config) {
        this.config = config;
    }

    protected Connection getConnection() {
        try {
            return DataSource.getInstance(config).getConnection();
        } catch (SQLException e) {
            throw new UnableToGetConnectionException(e.getMessage());
        }
    }

    public void save(BuyPositionDto buyPositionDto) {

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_TRANSACTION);
            preparedStatement.setObject(1, buyPositionDto.getUserId());
            preparedStatement.setString(2, buyPositionDto.getName());
            preparedStatement.setDouble(3, buyPositionDto.getQuantity());
            preparedStatement.setObject(4, LocalDateTime.now());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public List<UserTransaction> getTransactionsByUserId(Integer userId) {

        try {
            List<UserTransaction> transactionList = new ArrayList<>();
            UserTransaction userTransaction;

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TRANSACTION_BY_USERID);
            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                userTransaction = UserTransaction.builder()
                        .name(rs.getString(NAME))
                        .quantity(rs.getInt(QUANTITY))
                        .date(rs.getDate(DATE))
                        .build();
                transactionList.add(userTransaction);

            }
            return transactionList;

        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }
}
