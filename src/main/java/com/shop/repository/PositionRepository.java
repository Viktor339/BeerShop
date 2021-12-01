package com.shop.repository;

import com.shop.config.Config;
import com.shop.model.Position;
import com.shop.service.exception.UnableToExecuteQueryException;
import com.shop.service.exception.UnableToGetConnectionException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class PositionRepository {
    private static final String SAVE_BOTTLE_BEER_POSITION =
            "insert into positions (name,beerType,alcoholPercentage,bitterness,containerType,containerVolume,quantity,created,modified) values (?,?,?,?,?,?,?,?,?)";
    private static final String SAVE_DRAFT_BEER_POSITION =
            "insert into positions (name,beerType,alcoholPercentage,bitterness,containerType,availableLiters,created,modified) values (?,?,?,?,?,?,?,?)";
    private static final String GET_POSITION_BY_NAME_AND_CONTAINER_TYPE = "select * from positions where name =? and containerType= ?";
    private static final String UPDATE_POSITION = "update positions set containerVolume=?,quantity=? where name=? and containerType=?";
    private final Config config;

    public PositionRepository() {
        config = new Config();
    }

    protected Connection getConnection() {
        try {
            return DataSource.getInstance(config).getConnection();
        } catch (SQLException e) {
            throw new UnableToGetConnectionException(e.getMessage());
        }
    }

    public boolean getPositionByNameOrContainerType(String name, String containerType) {

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_POSITION_BY_NAME_AND_CONTAINER_TYPE);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, containerType);

            ResultSet rs = preparedStatement.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public Position savePosition(String name, String beerType, int alcoholPercentage, String bitterness, String containerType, String containerVolume, String quantity, Timestamp created, Timestamp modified) {

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_BOTTLE_BEER_POSITION);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, beerType);
            preparedStatement.setInt(3, alcoholPercentage);
            preparedStatement.setString(4, bitterness);
            preparedStatement.setString(5, containerType);
            preparedStatement.setString(6, containerVolume);
            preparedStatement.setString(7, quantity);
            preparedStatement.setTimestamp(8, created);
            preparedStatement.setTimestamp(9, modified);

            Position position = Position.builder()
                    .name(name)
                    .beerType(beerType)
                    .alcoholPercentage(alcoholPercentage)
                    .bitterness(bitterness)
                    .containerType(containerType)
                    .containerVolume(containerVolume)
                    .quantity(quantity)
                    .build();

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                position.setId(generatedKeys.getLong(1));
            }

            return position;
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public Position savePosition(String name, String beerType, int alcoholPercentage, String bitterness, String containerType, String availableLiters, Timestamp created, Timestamp modified) {

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_DRAFT_BEER_POSITION, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, beerType);
            preparedStatement.setInt(3, alcoholPercentage);
            preparedStatement.setString(4, bitterness);
            preparedStatement.setString(5, containerType);
            preparedStatement.setString(6, availableLiters);
            preparedStatement.setTimestamp(7, created);
            preparedStatement.setTimestamp(8, modified);

            Position position = Position.builder()
                    .name(name)
                    .beerType(beerType)
                    .alcoholPercentage(alcoholPercentage)
                    .bitterness(bitterness)
                    .containerType(containerType)
                    .availableLitres(availableLiters)
                    .build();

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                position.setId(generatedKeys.getLong(1));
            }

            return position;
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }


    public Position update(String containerVolume, String quantity, String name, String containerType) {

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_POSITION, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, containerVolume);
            preparedStatement.setString(2, quantity);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, containerType);

            Position position = Position.builder()
                    .name(name)
                    .containerType(containerType)
                    .containerVolume(containerVolume)
                    .quantity(quantity)
                    .build();

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                position.setId(generatedKeys.getLong(1));
            }

            return position;
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }


    }
}
