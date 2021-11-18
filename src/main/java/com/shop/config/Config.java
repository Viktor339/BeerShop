package com.shop.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Properties;

public class Config implements ServletContextListener {
    private static final String ATTRIBUTE_NAME = "config";
    private final Properties config = new Properties();

    @Override
    public void contextInitialized(ServletContextEvent event) {

        try {
            config.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
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

    public String getProperty(String key) {
        return config.getProperty(key);
    }
}
