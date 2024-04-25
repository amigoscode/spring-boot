package com.amigoscode;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class SpringAndSpringBootApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(
                SpringAndSpringBootApplication.class,
                args
        );
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
             // System.out.println(beanDefinitionName);
        }
        System.out.println(beanDefinitionNames.length);
    }

    // @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    // @SessionScope
    // @ApplicationScope
    @Bean
    public String redBean() {
        return "Manchester United";
    }

    @Bean
    public String blueBean() {
        return "Chelsea";
    }

    @Bean
    // @Autowired
    CommandLineRunner commandLineRunner(String redBean,
                                        String blueBean,
                                        UserService userService) {
        return args -> {
            System.out.println("Hello From CommandLineRunner 1");
            System.out.println(redBean);
            System.out.println(blueBean);
            System.out.println(userService.getUsers());
            System.out.println();
        };
    }

    @Bean
        // @Autowired
    CommandLineRunner commandLineRunner2(String redBean,
                                         String blueBean,
                                         UserService userService) {
        return args -> {
            System.out.println("Hello From CommandLineRunner 2");
            System.out.println(redBean);
            System.out.println(blueBean);
            System.out.println(userService.getUserById(2));
            System.out.println(userService.getUserById(3));
        };
    }

    public record User(int id, String name) {}

    @Service
    public class UserService {

        public UserService() {
            System.out.println(" UserService constructor");
        }

        @PostConstruct
        public void init() {
            System.out.println("fill redis cache...");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("clear redis cache...");
        }

        public List<User> getUsers() {
            return List.of(
                    new User(1, "John Doe"),
                    new User(2, "Jamila Balde")
            );
        }

        public Optional<User> getUserById(int id) {
            return getUsers().stream()
                    .filter(u -> u.id == id)
                    .findFirst();
        }
    }

}
