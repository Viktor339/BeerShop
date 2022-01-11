package com.shop.repository;

import com.shop.model.BeerInfo;
import com.shop.model.BottleBeerData;
import com.shop.model.Position;
import com.shop.servlet.dto.AddPositionDto;
import com.shop.servlet.dto.PositionDto;
import org.hibernate.Session;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class PositionRepository {

    private static final String GET_POSITION_BY_NAME_AND_CONTAINER_TYPE = "select * from positions where name =? and container_type= ?";
    private static final String GET_POSITION_BY_ID = "select * from positions where id =?";
    private static final String SELECT_POSITION_BY_ID = "select * from positions where id=?";
    private static final String SELECT_ACTIVE_POSITION =
            "select * from positions where id in (" +
                    "select id from positions  where CAST(beer_Info ->> 'quantity' AS INTEGER ) > 0 or CAST(beer_Info ->> 'availableLiters' AS DOUBLE PRECISION) > 0.0" +
                    ")" +
                    "    order by id\n" +
                    "limit ? offset ?";

    public boolean existsPositionById(Integer id, Session session) {

        List<Position> position = session.createNativeQuery(GET_POSITION_BY_ID, Position.class)
                .setParameter(1, id)
                .list();

        return !position.isEmpty();
    }


    public AddPositionDto save(AddPositionDto addPositionDto, Session session) {

        Position position = Position.builder()
                .name(addPositionDto.getName())
                .beerType(addPositionDto.getBeerType())
                .alcoholPercentage(addPositionDto.getAlcoholPercentage())
                .bitterness(addPositionDto.getBitterness())
                .containerType(addPositionDto.getContainerType())
                .beerInfo(addPositionDto.getBeerInfo())
                .created(Timestamp.from(Instant.now()))
                .modified(Timestamp.from(Instant.now()))
                .build();

        session.save(position);

        addPositionDto.setId(position.getId());
        return addPositionDto;
    }


    public void update(BottleBeerData beerInfo, Integer id, Session session) {

        final Position position = session.get(Position.class, id);

        position.setBeerInfo(BeerInfo.builder()
                .containerVolume(beerInfo.getContainerVolume())
                .quantity(beerInfo.getQuantity())
                .build());
        position.setModified(Timestamp.from(Instant.now()));

    }


    public <T> Optional<PositionDto> findPositionById(Long id, Session session) {

        Position position = session.createNativeQuery(SELECT_POSITION_BY_ID, Position.class)
                .setParameter(1, id)
                .getSingleResult();

        PositionDto positionDto = PositionDto.builder()
                .id(position.getId())
                .name(position.getName())
                .beerType(position.getBeerType())
                .alcoholPercentage(position.getAlcoholPercentage())
                .bitterness(position.getBitterness())
                .containerType(position.getContainerType())
                .beerInfo(position.getBeerInfo())
                .build();

        return Optional.of(positionDto);
    }


    public void updatePositionAfterPurchase(PositionDto positionDto, Session session) {

        Position position = session.get(Position.class, positionDto.getId());
        position.setBeerInfo(positionDto.getBeerInfo());

    }

    public boolean existsPositionByNameAndContainerType(String name, String containerType, Session session) {

        List<Position> positionList = session.createNativeQuery(GET_POSITION_BY_NAME_AND_CONTAINER_TYPE, Position.class)
                .setParameter(1, name)
                .setParameter(2, containerType)
                .list();

        return !positionList.isEmpty();

    }

    public List<Position> getPositionByBeerInfo(Integer validatedPageSize, Integer page, Session session) {

        return session.createNativeQuery(SELECT_ACTIVE_POSITION, Position.class)
                .setParameter(1, validatedPageSize)
                .setParameter(2, validatedPageSize * (page - 1))
                .list();
    }
}
