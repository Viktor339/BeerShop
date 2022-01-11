package com.shop.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class TransactionalHandler {

    private final SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction = null;

    public TransactionalHandler() {
        Configuration configuration = new Configuration();
        configuration.configure();
        sessionFactory = configuration.buildSessionFactory();
    }


    public void doTransaction(TransactionalOperation transactionalOperation) {

        try {
            if (transaction == null) {
                beginTransaction();
            }

            transactionalOperation.doOperation(session);

            session.getTransaction().commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;

        } finally {
            session.close();
        }
    }

    public Session getCurrentSession() {
        return session;
    }

    public void beginTransaction() {
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }
}
