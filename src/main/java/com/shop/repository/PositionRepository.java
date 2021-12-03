package com.shop.repository;

import com.shop.config.Config;
import com.shop.model.BeerInfo;
import com.shop.model.BottleBeerData;
import com.shop.model.Position;
import com.shop.service.exception.UnableToExecuteQueryException;
import com.shop.service.exception.UnableToGetConnectionException;
import com.shop.servlet.dto.AddPositionResponse;
import org.codehaus.jackson.map.ObjectMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class PositionRepository {

    private static final String SAVE_POSITION =
            "insert into positions (name,beerType,alcoholPercentage,bitterness,containerType,created,modified,beerInfo) values (?,?,?,?,?,?,?,to_json(?::json))";
    private static final String GET_POSITION_BY_NAME_AND_CONTAINER_TYPE = "select * from positions where name =? and containerType= ?";
    private static final String UPDATE_POSITION = "update positions set beerInfo=to_json(?::json),modified=? where name=? and containerType=?";
    private final Config config;
    private final ObjectMapper objectMapper;

    public PositionRepository(ObjectMapper objectMapper, Config config) {
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


    public AddPositionResponse save(AddPositionResponse addPositionResponse) {

        try {
            String json = objectMapper.writeValueAsString(addPositionResponse.getBeerInfo());

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_POSITION, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, addPositionResponse.getName());
            preparedStatement.setString(2, addPositionResponse.getBeerType());
            preparedStatement.setDouble(3, addPositionResponse.getAlcoholPercentage());
            preparedStatement.setInt(4, addPositionResponse.getBitterness());
            preparedStatement.setString(5, addPositionResponse.getContainerType());
            preparedStatement.setObject(6, LocalDateTime.now());
            preparedStatement.setObject(7, LocalDateTime.now());
            preparedStatement.setString(8, json);

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                addPositionResponse.setId(generatedKeys.getLong(1));
            }

            return addPositionResponse;
        } catch (Exception e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }


    public Position update(Double containerVolume, Integer quantity, String name, String containerType) {

        try {
            BeerInfo beerInfo = new BottleBeerData(containerVolume, quantity);
            String json = objectMapper.writeValueAsString(beerInfo);

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_POSITION, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, json);
            preparedStatement.setObject(2, LocalDateTime.now());
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, containerType);

            Position position = Position.builder()
                    .name(name)
                    .containerType(containerType)
                    .beerInfo(beerInfo)
                    .build();

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                position.setId(generatedKeys.getLong(1));
            }

            return position;
        } catch (Exception e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }
}
