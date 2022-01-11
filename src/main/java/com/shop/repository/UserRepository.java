package com.shop.repository;

import com.shop.model.User;
import com.shop.service.exception.UserNotFoundException;
import org.hibernate.Session;

import java.util.List;

public class UserRepository {

    private static final String SELECT_USER_BY_NAME_AND_PASSWORD = "select * from users where name=? and password=?";
    private static final String SELECT_USERID_BY_UUID = "select * from users where uuid=? ";
    private static final String SELECT_USER_BY_NAME_OR_EMAIL = "select * from users where name =? or email= ?";

    public boolean existsUserByNameOrEmail(String name, String email, Session session) {

        List<User> userList = session.createNativeQuery(SELECT_USER_BY_NAME_OR_EMAIL, User.class)
                .setParameter(1, name)
                .setParameter(2, email)
                .list();

        return !userList.isEmpty();
    }


    public void saveUser(String name, String password, String email, String uuid, String role, Session session) {

        session.save(User.builder()
                .name(name)
                .password(password)
                .email(email)
                .UUID(uuid)
                .role(role)
                .build());
    }


    public User getUserByNameAndPassword(String name, String password, Session session) {

        User user = session.createNativeQuery(SELECT_USER_BY_NAME_AND_PASSWORD, User.class)
                .setParameter(1, name)
                .setParameter(2, password)
                .getSingleResult();

        if (user == null) {
            throw new UserNotFoundException("Incorrect username or password");
        }
        return user;
    }


    public Integer getUserIdByUUID(Object uuid, Session session) {

        User user = session.createNativeQuery(SELECT_USERID_BY_UUID, User.class)
                .setParameter(1, uuid)
                .getSingleResult();

        return user.getId();
    }
}
