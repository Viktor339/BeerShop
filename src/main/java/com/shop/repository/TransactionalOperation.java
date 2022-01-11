package com.shop.repository;

import org.hibernate.Session;

public interface TransactionalOperation {
    void doOperation(Session session);
}
