package com.shop.config;

import com.shop.service.exception.ConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final String DRIVER = "jdbc.dbcDriver";
    private static final String PASSWORD = "jdbc.Password";
    private static final String URL = "jdbc.URL";
    private static final String USERNAME = "jdbc.Username";
    private static final String PROPERTIES_FILE_NAME = "application.properties";
    private static final String MIN_PERCENTAGE = "validator.percentage.min";
    private static final String MAX_PERCENTAGE = "validator.percentage.max";
    private static final String MIN_CONTAINER_VOLUME = "validator.container.min";
    private static final String MAX_CONTAINER_VOLUME = "validator.container.max";
    private static final String MIN_BITTERNESS = "validator.bitterness.min";
    private static final String MAX_BITTERNESS = "validator.bitterness.max";
    private static final String MIN_USER_PAGE_SIZE = "validator.user.pageSize.min";
    private static final String MAX_USER_PAGE_SIZE = "validator.user.pageSize.max";




    private final Properties config = new Properties();

    public Config() {
        InputStream input = Config.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        try {
            config.load(input);
        } catch (IOException e) {
            throw new ConfigException(e.getMessage());
        }
    }

    public String getDriver() {
        return config.getProperty(DRIVER);
    }

    public String getPassword() {
        return config.getProperty(PASSWORD);
    }

    public String getURL() {
        return config.getProperty(URL);
    }

    public String getUsername() {
        return config.getProperty(USERNAME);
    }

    public Double getMinAlcoholPercentage() {
        return Double.parseDouble(config.getProperty(MIN_PERCENTAGE));
    }

    public Double getMaxAlcoholPercentage() {
        return Double.parseDouble(config.getProperty(MAX_PERCENTAGE));
    }

    public Double getMinContainerVolume() {
        return Double.parseDouble(config.getProperty(MIN_CONTAINER_VOLUME));
    }

    public Double getMaxContainerVolume() {
        return Double.parseDouble(config.getProperty(MAX_CONTAINER_VOLUME));
    }

    public Integer getMinBitterness() {
        return Integer.parseInt(config.getProperty(MIN_BITTERNESS));
    }

    public Integer getMAxBitterness() {
        return Integer.parseInt(config.getProperty(MAX_BITTERNESS));
    }

    public Integer getMinUserPageSize() {
        return Integer.parseInt(config.getProperty(MIN_USER_PAGE_SIZE));
    }

    public Integer getMaxUserPageSize() {
        return Integer.parseInt(config.getProperty(MAX_USER_PAGE_SIZE));
    }
}
