package ru.webrise.subscriptionaccount.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
    @NotNull(message = "Fill in the email")
        @Size(min = 8, message = "Email can't be less than 8")
        @Email(message = "The email must be in the format test@test.com")
        String email,
    @NotNull(message = "Fill in the first name")
        @Size(min = 1, message = "First name can't be less than 1")
        String firstName,
    @NotNull(message = "Fill in the last name")
        @Size(min = 1, message = "Last name can't be less than 1")
        String lastName) {}
