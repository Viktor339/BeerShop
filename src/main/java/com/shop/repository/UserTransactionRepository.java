package com.shop.repository;

import com.shop.config.Config;
import com.shop.model.BottleBuyBeerQuantity;
import com.shop.model.BuyBeerQuantity;
import com.shop.model.DraftBuyBeerQuantity;
import com.shop.model.UserTransaction;
import com.shop.service.exception.UnableToExecuteQueryException;
import com.shop.service.exception.UnableToGetConnectionException;
import com.shop.service.exception.UnableToPerformSerializationException;
import com.shop.servlet.dto.BuyPositionDto;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class UserTransactionRepository {
    private static final String SELECT_TRANSACTION_BY_USERID = "select * from users_transactions where user_id=? order by id limit ? offset ?";
    private static final String SELECT_ALL_TRANSACTIONS = "select * from users_transactions order by id limit ? offset ?";
    public static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    private static final String SAVE_TRANSACTION =
            "insert into users_transactions (user_id,position_id,quantity,created,quantity_class_type) values (?,?,to_json(?::json),?,?)";
    private static final String POSITION_ID = "position_id";
    private static final String USER_ID = "user_id";
    private static final String QUANTITY = "quantity";
    private static final String CREATED = "created";
    private static final String QUANTITY_CLASS_TYPE = "quantity_class_type";
    private final Config config;
    private final ObjectMapper objectMapper;


    public UserTransactionRepository(Config config, ObjectMapper objectMapper) {
        this.config = config;
        this.objectMapper = objectMapper;
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
            String json = objectMapper.writeValueAsString(buyPositionDto.getQuantity());

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_TRANSACTION);
            preparedStatement.setObject(1, buyPositionDto.getUserId());
            preparedStatement.setInt(2, buyPositionDto.getPosition().getId());
            preparedStatement.setString(3, json);

            Instant eventOccurred = Instant.now();
            preparedStatement.setTimestamp(4, Timestamp.from(eventOccurred), UTC);
            preparedStatement.setString(5, buyPositionDto.getQuantityType());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public List<UserTransaction> getTransactionsByUserId(Integer userId, Integer validPageSize, Integer page) {

        try {
            List<UserTransaction> transactionList = new ArrayList<>();

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_TRANSACTION_BY_USERID);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, validPageSize);
            preparedStatement.setInt(3, validPageSize * (page - 1));

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                Class<? extends BuyBeerQuantity> classType = BottleBuyBeerQuantity.class;
                if (rs.getString(QUANTITY_CLASS_TYPE).equals("DraftBuyBeerQuantity")) {
                    classType = DraftBuyBeerQuantity.class;
                }

                transactionList.add(UserTransaction.builder()
                        .positionId(rs.getInt(POSITION_ID))
                        .quantity(objectMapper.readValue(rs.getString(QUANTITY), classType))
                        .created(rs.getTimestamp(CREATED, UTC).toInstant())
                        .build());

            }
            return transactionList;

        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        } catch (IOException e) {
            throw new UnableToPerformSerializationException(e.getMessage());
        }
    }

    public List<UserTransaction> getAllTransactions(Integer validPageSize, Integer page) {

        try {
            List<UserTransaction> transactionList = new ArrayList<>();

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_TRANSACTIONS);
            preparedStatement.setInt(1, validPageSize);
            preparedStatement.setInt(2, validPageSize * (page - 1));


            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {

                transactionList.add(UserTransaction.builder()
                        .positionId(rs.getInt(POSITION_ID))
                        .userId(rs.getInt(USER_ID))
                        .build());

            }
            return transactionList;

        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }
}
