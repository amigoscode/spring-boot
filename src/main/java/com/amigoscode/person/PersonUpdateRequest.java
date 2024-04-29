package com.amigoscode.person;

public record PersonUpdateRequest(
        String name,
        Integer age,
        String email
) {
}
