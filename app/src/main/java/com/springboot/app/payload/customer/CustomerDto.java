package com.springboot.app.payload.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerDto {
    private Long id;
    @NotEmpty(message = "First name cannot be null or empty")
    private String firstName;
    @NotEmpty(message = "Last name cannot be null or empty")
    private String lastName;
    @Email(message = "Invalid email. Please provide something like this: example@example")
    private String email;
}
