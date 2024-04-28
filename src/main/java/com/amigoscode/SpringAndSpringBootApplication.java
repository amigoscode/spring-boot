package com.amigoscode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/person")
@SpringBootApplication
public class SpringAndSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                SpringAndSpringBootApplication.class,
                args
        );
    }

    @Bean
    CommandLineRunner commandLineRunner(ObjectMapper objectMapper) throws JsonProcessingException {
        String personString = "{\"id\":1,\"name\":\"John Doe\",\"age\":2, \"gender\":\"MALE\"}";
        Person person = objectMapper.readValue(personString, Person.class);
        System.out.println(person);
        System.out.println(objectMapper.writeValueAsString(person));
        return args -> {

        };
    }

    public enum Gender {MALE, FEMALE}

    public enum SortingOder {ASC, DESC}

    /*
    public record Person(Integer id,
                         String name,
                         Integer age,
                         Gender gender) {

    }

     */

    public static class Person {
        private final Integer id;
        private final String name;
        private final Integer age;
        private final Gender gender;

        public Person(Integer id, String name, Integer age, Gender gender) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.gender = gender;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }

        public Gender getGender() {
            return gender;
        }

        @JsonIgnore
        public String getPassword() {
            return "password";
        }

        public String getProfile() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", gender=" + gender +
                    '}';
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", gender=" + gender +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return Objects.equals(id, person.id) && Objects.equals(name, person.name) && Objects.equals(age, person.age) && gender == person.gender;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, age, gender);
        }
    }

    private static final AtomicInteger idCounter = new AtomicInteger(0);

    public static List<Person> people = new ArrayList<>();

    static {
        people.add(
                new Person(
                        idCounter.incrementAndGet(),
                        "John",
                        20,
                        Gender.MALE
                )
        );
        people.add(
                new Person(
                        idCounter.incrementAndGet(),
                        "Mariam",
                        18,
                        Gender.FEMALE)
        );
        people.add(
                new Person(
                        idCounter.incrementAndGet(),
                        "Samba",
                        30,
                        Gender.MALE)
        );
    }

    @GetMapping
    public List<Person> getPersons(
            HttpMethod httpMethod,
            ServletRequest request,
            ServletResponse response,
            @RequestHeader("Content-Type") String contentType,
            @RequestParam(
                    value = "sort",
                    required = false,
                    defaultValue = "ASC"
            ) SortingOder sort,
            @RequestParam(
                    value = "limit",
                    required = false,
                    defaultValue = "10"
            ) Integer limit) {
        if (sort == SortingOder.ASC) {

            System.out.println(httpMethod);
            System.out.println(request.getLocalAddr());
            System.out.println(response.isCommitted());
            System.out.println(contentType);

            return people.stream()
                    .sorted(Comparator.comparing(Person::getId))
                    .collect(Collectors.toList());
        }
        return people.stream()
                .sorted(Comparator.comparing(Person::getId).reversed())
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<Person>> getPersonById(
            @PathVariable("id") Integer id
    ) {
        Optional<Person> person = people.stream()
                .filter(p -> p.id.equals(id))
                .findFirst();
        return ResponseEntity.ok().body(person);
    }

    @DeleteMapping("{id}")
    public void deletePersonById(@PathVariable("id") Integer id) {
        people.removeIf(person -> person.id.equals(id));
    }

    @PostMapping
    public void addPerson(@RequestBody Person person) {
        people.add(
                new Person(
                        idCounter.incrementAndGet(),
                        person.name,
                        person.getAge(),
                        person.gender
                )
        );
    }

    public record PersonUpdateRequest(
            String name,
            Integer age
    ) {
    }

    @PutMapping("{id}")
    public void updatePerson(@PathVariable("id") Integer id,
                             @RequestBody PersonUpdateRequest request) {
        // find person by id
        people.stream()
                .filter(p -> p.id.equals(id))
                .findFirst()
                .ifPresent(p -> {
                    var index = people.indexOf(p);

                    if (request.name != null &&
                            !request.name.isEmpty() &&
                            !request.name.equals(p.name)) {
                        Person person = new Person(
                                p.id,
                                request.name,
                                p.getAge(),
                                p.getGender()

                        );
                        people.set(index, person);
                    }
                    if (request.age != null
                            && !request.age.equals(p.age)) {
                        Person person = new Person(
                                p.id,
                                p.name,
                                request.age,
                                p.getGender()

                        );
                        people.set(index, person);
                    }
                });
    }

}
