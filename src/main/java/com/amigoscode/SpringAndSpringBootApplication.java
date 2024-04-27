package com.amigoscode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@SpringBootApplication
public class SpringAndSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                SpringAndSpringBootApplication.class,
                args
        );
    }

    public enum Gender {MALE, FEMALE}

    public enum SortingOder {ASC, DESC}

    public record Person(Integer id,
                         String name,
                         Integer age,
                         Gender gender) {

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
            return people.stream()
                    .sorted(Comparator.comparing(Person::id))
                    .collect(Collectors.toList());
        }
        return people.stream()
                .sorted(Comparator.comparing(Person::id).reversed())
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public Optional<Person> getPersonById(
            @PathVariable("id") Integer id
    ) {
        return people.stream()
                .filter(person -> person.id.equals(id))
                .findFirst();
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
                        person.age(),
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
                                p.age(),
                                p.gender()

                        );
                        people.set(index, person);
                    }
                    if (request.age != null
                            && !request.age.equals(p.age)) {
                        Person person = new Person(
                                p.id,
                                p.name,
                                request.age,
                                p.gender()

                        );
                        people.set(index, person);
                    }
                });
    }

}
