package com.shop.repository;

import com.shop.config.Config;
import com.shop.service.exception.UnableToExecuteQueryException;
import com.shop.service.exception.UnableToGetConnectionException;
import com.shop.servlet.dto.BuyPositionTransactionDTO;
import com.shop.servlet.dto.GetUserHistoryTransactionDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class TransactionRepository {
    private static final String SELECT_TRANSACTION_BY_UUID = "select * from transactions where uuid=?";
    private static final String SAVE_TRANSACTION =
            "insert into transactions (uuid,name,quantity,date) values (?,?,?,?)";
    private final Config config;


    public TransactionRepository(Config config) {
        this.config = config;
    }

    protected Connection getConnection() {
        try {
            return DataSource.getInstance(config).getConnection();
        } catch (SQLException e) {
            throw new UnableToGetConnectionException(e.getMessage());
        }
    }

    public void save(BuyPositionTransactionDTO buyPositionTransactionDTO) {


        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_TRANSACTION);
            preparedStatement.setObject(1, buyPositionTransactionDTO.getUuid());
            preparedStatement.setString(2, buyPositionTransactionDTO.getName());
            preparedStatement.setInt(3, buyPositionTransactionDTO.getQuantity());
            preparedStatement.setObject(4, LocalDateTime.now());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }

    }

    public List<GetUserHistoryTransactionDTO> getTransactionsByUUID(Object uuid) {


        try {
            List<GetUserHistoryTransactionDTO> transactionList = new ArrayList<>();
            GetUserHistoryTransactionDTO getUserHistoryTransactionDTO;

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TRANSACTION_BY_UUID);
            preparedStatement.setObject(1, uuid);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                getUserHistoryTransactionDTO = GetUserHistoryTransactionDTO.builder()
                        .name(rs.getString("name"))
                        .quantity(rs.getInt("quantity"))
                        .date(rs.getString("date"))
                        .build();
                transactionList.add(getUserHistoryTransactionDTO);

            }
            return transactionList;

        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

}
