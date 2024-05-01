package com.amigoscode.person;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    boolean existsByEmail(String email);
}
