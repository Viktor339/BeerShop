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

    public String getMinAlcoholPercentage() {
        return config.getProperty(MIN_PERCENTAGE);
    }

    public String getMaxAlcoholPercentage() {
        return config.getProperty(MAX_PERCENTAGE);
    }

    public String getMinContainerVolume() {
        return config.getProperty(MIN_CONTAINER_VOLUME);
    }

    public String getMaxContainerVolume() {
        return config.getProperty(MAX_CONTAINER_VOLUME);
    }

    public String getMinBitterness() {
        return config.getProperty(MIN_BITTERNESS);
    }

    public String getMAxBitterness() {
        return config.getProperty(MAX_BITTERNESS);
    }
}
