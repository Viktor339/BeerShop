package com.shop.repository;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.shop.config.Config;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static DataSource datasource;
    private final ComboPooledDataSource connectionPool;


    private DataSource(Config config) throws PropertyVetoException {
        connectionPool = new ComboPooledDataSource();
        connectionPool.setDriverClass(config.getDriver());
        connectionPool.setJdbcUrl(config.getURL());
        connectionPool.setUser(config.getUsername());
        connectionPool.setPassword(config.getPassword());
        connectionPool.setMinPoolSize(5);
        connectionPool.setAcquireIncrement(5);
        connectionPool.setMaxPoolSize(20);
    }

    public static DataSource getInstance(Config config) throws PropertyVetoException {
        if (datasource == null) {
            datasource = new DataSource(config);
        }
        return datasource;
    }

    public Connection getConnection() throws SQLException {
        return this.connectionPool.getConnection();
    }

}
