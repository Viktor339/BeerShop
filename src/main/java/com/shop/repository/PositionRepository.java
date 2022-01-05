package com.shop.repository;

import com.shop.config.Config;
import com.shop.model.BeerInfo;
import com.shop.model.BottleBeerData;
import com.shop.model.Position;
import com.shop.service.exception.UnableToExecuteQueryException;
import com.shop.service.exception.UnableToGetConnectionException;
import com.shop.service.exception.UnableToPerformSerializationException;
import com.shop.servlet.dto.AddPositionDto;
import com.shop.servlet.dto.PositionDto;
import org.codehaus.jackson.map.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

public class PositionRepository {

    private static final String SAVE_POSITION =
            "insert into positions (name,beer_type,alcohol_percentage,bitterness,container_type,created,modified,beer_info) values (?,?,?,?,?,?,?,to_json(?::json))";
    private static final String GET_POSITION_BY_NAME_AND_CONTAINER_TYPE = "select * from positions where name =? and container_type= ?";
    private static final String GET_POSITION_BY_ID = "select * from positions where id =?";
    private static final String UPDATE_POSITION = "update positions set beer_info=to_json(?::json),modified=? where id=?";
    private static final String UPDATE_POSITION_AFTER_PURCHASE = "update positions set beer_info=to_json(?::json) where name=? and container_type=?";
    private static final String SELECT_POSITION_BY_ID = "select * from positions where id=?";
    private static final String SELECT_ACTIVE_POSITION =
            "select * from positions where id not in (\n" +
                    "    select id\n" +
                    "    from positions\n" +
                    "    where beer_info::jsonb @> '{\"quantity\":0}'\n" +
                    "       or beer_info::jsonb @> '{\"availableLiters\":0.0}'\n)" +
                    "    order by id\n" +
                    "limit ? offset ?";
    public static final Calendar UTC = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String BEER_TYPE = "beer_type";
    private static final String ALCOHOL_PERCENTAGE = "alcohol_percentage";
    private static final String BITTERNESS = "bitterness";
    private static final String CONTAINER_TYPE = "container_type";
    private static final String BEER_INFO = "beer_info";

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

            Configuration configuration = new Configuration();
            configuration.configure();

            SessionFactory sessionFactory = configuration.buildSessionFactory();
            Session session = sessionFactory.openSession();

            session.beginTransaction();

            Position position = Position.builder()
                    .name(addPositionDto.getName())
                    .beerType(addPositionDto.getBeerType())
                    .alcoholPercentage(addPositionDto.getAlcoholPercentage())
                    .bitterness(addPositionDto.getBitterness())
                    .containerType(addPositionDto.getContainerType())
                    .beerInfo(addPositionDto.getBeerInfo())
                    .build();

            session.save(position);
            session.getTransaction().commit();

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
            Instant eventOccurred = Instant.now();
            preparedStatement.setTimestamp(2, Timestamp.from(eventOccurred), UTC);
            preparedStatement.setLong(3, id);

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }


    public <T> Optional<PositionDto> findPositionById(Long id, Class<T> clazz) {
        PositionDto positionDto = null;

        try {

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_POSITION_BY_ID, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                positionDto = PositionDto.builder()
                        .id(rs.getInt(ID))
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
        return Optional.ofNullable(positionDto);
    }


    public void updatePositionAfterPurchase(PositionDto positionDto) {


        try {
            String json = objectMapper.writeValueAsString(positionDto.getBeerInfo());

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_POSITION_AFTER_PURCHASE);
            preparedStatement.setString(1, json);
            preparedStatement.setString(2, positionDto.getName());
            preparedStatement.setString(3, positionDto.getContainerType());

            preparedStatement.executeUpdate();

        } catch (Exception e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public boolean existsPositionByNameAndContainerType(String name, String containerType) {

        Configuration configuration = new Configuration();
        configuration.configure();

        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        String sql = "select * from positions where name =? and container_type= ?";
        List<Position> positionList = session.createNativeQuery(sql, Position.class)
                .setParameter(1, name)
                .setParameter(2, containerType)
                .list();

        session.getTransaction().commit();

        return !positionList.isEmpty();
    }

    public List<Position> getPositionByBeerInfo(Integer validatedPageSize, Integer page) {

        List<Position> positionList = new ArrayList<>();

        try {

            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACTIVE_POSITION);
            preparedStatement.setLong(1, validatedPageSize);
            preparedStatement.setInt(2, validatedPageSize * (page - 1));

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                positionList.add(Position.builder()
                        .id(rs.getInt(ID))
                        .build());
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }

        return positionList;
    }
}
