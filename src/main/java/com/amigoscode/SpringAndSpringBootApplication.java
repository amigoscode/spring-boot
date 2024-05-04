package com.amigoscode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
public class SpringAndSpringBootApplication {

    @Value("${app.stripe.api-key}")
    private String stripeApiKey;

    @Value("${spring.application.name}")
    private String appName;

    public static void main(String[] args) {
        System.out.println("args" + Arrays.toString(args));
        SpringApplication.run(
                SpringAndSpringBootApplication.class,
                args
        );
    }

    @Bean
    CommandLineRunner commandLineRunner(Environment environment,
                                        StripeConfig stripeConfig) {
        System.out.println(stripeApiKey);
        System.out.println(environment.getProperty("app.stripe.url"));
        System.out.println(stripeConfig);
        return args -> {};
    }

}
