package com.shop.repository;

import com.shop.config.Config;
import com.shop.model.BeerInfo;
import com.shop.model.Position;
import com.shop.service.exception.UnableToExecuteQueryException;
import com.shop.service.exception.UnableToGetConnectionException;
import com.shop.service.exception.UnableToPerformSerializationException;
import com.shop.servlet.dto.AddPositionDto;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;

public class PositionRepository {

    private static final String SAVE_POSITION =
            "insert into positions (name,beerType,alcoholPercentage,bitterness,containerType,created,modified,beerInfo) values (?,?,?,?,?,?,?,to_json(?::json))";
    private static final String GET_POSITION_BY_NAME_AND_CONTAINER_TYPE = "select * from positions where name =? and containerType= ?";
    private static final String GET_POSITION_BY_ID = "select * from positions where id =?";
    private static final String UPDATE_POSITION = "update positions set beerInfo=to_json(?::json),modified=? where id=?";
    private static final String UPDATE_POSITION_AFTER_PURCHASE = "update positions set beerInfo=to_json(?::json) where name=? and containerType=?";
    private static final String SELECT_POSITION_BY_ID = "select * from positions where id=?";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String BEER_TYPE = "beerType";
    private static final String ALCOHOL_PERCENTAGE = "alcoholPercentage";
    private static final String BITTERNESS = "bitterness";
    private static final String CONTAINER_TYPE = "containerType";
    private static final String BEER_INFO = "beerInfo";

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

    public boolean existsPositionById(Long id) {

        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_POSITION_BY_ID);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }


    public AddPositionDto save(AddPositionDto addPositionDto) {

        try {
            String json = objectMapper.writeValueAsString(addPositionDto.getBeerInfo());

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_POSITION, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, addPositionDto.getName());
            preparedStatement.setString(2, addPositionDto.getBeerType());
            preparedStatement.setDouble(3, addPositionDto.getAlcoholPercentage());
            preparedStatement.setInt(4, addPositionDto.getBitterness());
            preparedStatement.setString(5, addPositionDto.getContainerType());
            preparedStatement.setObject(6, LocalDateTime.now());
            preparedStatement.setObject(7, LocalDateTime.now());
            preparedStatement.setString(8, json);

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                addPositionDto.setId(generatedKeys.getLong(1));
            }

            return addPositionDto;
        } catch (Exception e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }


    public void update(BeerInfo beerInfo, Long id) {

        try {
            String json = objectMapper.writeValueAsString(beerInfo);

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_POSITION);
            preparedStatement.setString(1, json);
            preparedStatement.setObject(2, LocalDateTime.now());
            preparedStatement.setLong(3, id);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }


    public <T> Optional<Position> findPositionById(Long id, Class<T> clazz) {
        Position position = null;

        try {

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_POSITION_BY_ID, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                position = Position.builder()
                        .id(rs.getLong(ID))
                        .name(rs.getString(NAME))
                        .beerType(rs.getString(BEER_TYPE))
                        .alcoholPercentage(rs.getDouble(ALCOHOL_PERCENTAGE))
                        .bitterness(rs.getInt(BITTERNESS))
                        .containerType(rs.getString(CONTAINER_TYPE))
                        .beerInfo((BeerInfo) objectMapper.readValue(rs.getString(BEER_INFO), clazz))
                        .build();
            }

        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        } catch (IOException e) {
            throw new UnableToPerformSerializationException(e.getMessage());
        }
        return Optional.ofNullable(position);
    }


    public void updatePositionAfterPurchase(Position position) {


        try {
            String json = objectMapper.writeValueAsString(position.getBeerInfo());

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_POSITION_AFTER_PURCHASE);
            preparedStatement.setString(1, json);
            preparedStatement.setString(2, position.getName());
            preparedStatement.setString(3, position.getContainerType());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public boolean existsPositionByNameOrContainerType(String name, String containerType) {

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
}
