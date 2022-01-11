package com.shop.repository;

import com.shop.model.Position;
import com.shop.model.User;
import com.shop.model.UserTransaction;
import com.shop.servlet.dto.BuyPositionDto;
import com.shop.servlet.dto.PositionDto;
import org.hibernate.Session;

import java.time.Instant;
import java.util.List;


public class UserTransactionRepository {
    private static final String SELECT_TRANSACTION_BY_USERID = "select * from users_transactions where user_id=? order by id limit ? offset ?";
    private static final String SELECT_ALL_TRANSACTIONS = "select * from users_transactions order by id limit ? offset ?";

    public void save(BuyPositionDto buyPositionDto, Session session) {

        PositionDto positionDto = buyPositionDto.getPositionDto();

        Position position = session.get(Position.class, positionDto.getId());
        User user = session.get(User.class, buyPositionDto.getUserId());

        UserTransaction userTransaction = UserTransaction.builder()
                .position(position)
                .quantity(buyPositionDto.getQuantity())
                .user(user)
                .created(Instant.now())
                .build();

        session.save(userTransaction);

    }

    public List<UserTransaction> getTransactionsByUserId(Integer userId, Integer validPageSize, Integer page, Session session) {

        return session.createNativeQuery(SELECT_TRANSACTION_BY_USERID, UserTransaction.class)
                .setParameter(1, userId)
                .setParameter(2, validPageSize)
                .setParameter(3, validPageSize * (page - 1))
                .list();
    }

    public List<UserTransaction> getAllTransactions(Integer validPageSize, Integer page, Session session) {

        return session.createNativeQuery(SELECT_ALL_TRANSACTIONS, UserTransaction.class)
                .setParameter(1, validPageSize)
                .setParameter(2, validPageSize * (page - 1))
                .list();
    }
}
