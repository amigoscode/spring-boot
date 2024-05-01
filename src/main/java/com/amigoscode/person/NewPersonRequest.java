package com.amigoscode.person;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record NewPersonRequest(@NotEmpty(message = "Name must no be null or empty") String name,
                               @Min(value = 16, message = "Age must be greater than 16") Integer age,
                               @NotNull(message = "Gender must no be null") Gender gender,
                               @Email(message = "Email must be valid") String email) {

}
