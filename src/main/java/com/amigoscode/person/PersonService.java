package com.amigoscode.person;

import com.amigoscode.SortingOrder;
import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final FakePersonRepository fakePersonRepository;
    private final PersonRepository personRepository;

    public PersonService(FakePersonRepository fakePersonRepository,
                         PersonRepository personRepository) {
        this.fakePersonRepository = fakePersonRepository;
        this.personRepository = personRepository;
    }

    public List<Person> getPeople(
            SortingOrder sort
    ) {
        return personRepository.findAll();
    }

    public Person getPersonById(Integer id) {
        return personRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Person with id: " + id + " does not exists"));

    }

    public void deletePersonById(Integer id) {
        boolean existsById = personRepository.existsById(id);
        if (!existsById) {
            throw new ResourceNotFoundException(
                    "Person with id: " + id + " does not exists"
            );
        }
        personRepository.deleteById(id);
    }

    public void addPerson(NewPersonRequest personRequest) {
        if(personRequest.email() != null && !personRequest.email().isEmpty()) {
            boolean exists = personRepository.existsByEmail(personRequest.email());
            if (exists) {
                throw new DuplicateResourceException("email taken");
            }
        }

        Person person = new Person(
                personRequest.name(),
                personRequest.age(),
                personRequest.gender(),
                personRequest.email()
        );

        personRepository.save(person);

    }

    public void updatePerson(Integer id,
                             PersonUpdateRequest request) {
        Person p = fakePersonRepository.getPeople().stream()
                .filter(person -> person.getId().equals(id))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Person with id: " + id + " does not exists")
                );

        var index = fakePersonRepository.getPeople().indexOf(p);

        if (request.name() != null &&
                !request.name().isEmpty() &&
                !request.name().equals(p.getName())) {
            Person person = new Person(
                    p.getId(),
                    request.name(),
                    p.getAge(),
                    p.getGender(),
                    p.getEmail()

            );
            fakePersonRepository.getPeople().set(index, person);
        }
        if (request.email() != null &&
                !request.email().isEmpty() &&
                !request.email().equals(p.getEmail())) {
            Person person = new Person(
                    p.getId(),
                    p.getName(),
                    p.getAge(),
                    p.getGender(),
                    request.email()

            );
            fakePersonRepository.getPeople().set(index, person);
        }
        if (request.age() != null
                && !request.age().equals(p.getAge())) {
            Person person = new Person(
                    p.getId(),
                    p.getName(),
                    request.age(),
                    p.getGender(),
                    p.getEmail()

            );
            fakePersonRepository.getPeople().set(index, person);
        }

    }

}