package com.shop.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

public class Config implements ServletContextListener {

    private static final String ATTRIBUTE_NAME = "config";
    private static final String DRIVER = "jdbc.dbcDriver";
    private static final String PASSWORD = "jdbc.Password";
    private static final String URL = "jdbc.URL";
    private static final String USERNAME = "jdbc.Username";
    private static final String PROPERTIES_FILE_NAME = "application.properties";
    private final Properties config = new Properties();

    @Override
    public void contextInitialized(ServletContextEvent event) {

        try {
            config.load(this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
        event.getServletContext().setAttribute(ATTRIBUTE_NAME, this);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // NOOP
    }

    public static Config getInstance(ServletContext context) {
        return (Config) context.getAttribute(ATTRIBUTE_NAME);
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
