package com.amigoscode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringAndSpringBootApplication {

    private final static Logger LOGGER =
            LoggerFactory.getLogger(SpringAndSpringBootApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(
                SpringAndSpringBootApplication.class,
                args
        );
        System.out.println("Hello");
        LOGGER.info("Hello World");
        LOGGER.debug("I am a debug message");
        LOGGER.warn("I am a warn message");
        LOGGER.error("I am a error message");
    }

}
