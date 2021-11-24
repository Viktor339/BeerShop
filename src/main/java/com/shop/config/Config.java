package com.shop.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final String DRIVER = "jdbc.dbcDriver";
    private static final String PASSWORD = "jdbc.Password";
    private static final String URL = "jdbc.URL";
    private static final String USERNAME = "jdbc.Username";
    private static final String PROPERTIES_FILE_NAME = "application.properties";
    private final Properties config = new Properties();

    public Config() {
        InputStream input = Config.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        try {
            config.load(input);
        } catch (IOException e) {
            e.printStackTrace();
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
}
