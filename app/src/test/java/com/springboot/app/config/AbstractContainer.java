package com.springboot.app.config;

import org.testcontainers.containers.MySQLContainer;

public class AbstractContainer {
    static final MySQLContainer MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer("mysql:5-debian");
        MY_SQL_CONTAINER.start();
    }
}
