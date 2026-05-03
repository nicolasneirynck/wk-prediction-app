package com.example.wk_prognose.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InputRegistrationDTO(
        @NotBlank(message = "{validation.registration.firstName.required}")
        String firstName,

        @NotBlank(message = "{validation.registration.lastName.required}")
        String lastName,

        @NotBlank(message = "{validation.registration.email.required}")
        @Email(message = "{validation.registration.email.invalid}")
        String email,

        @NotBlank(message = "{validation.registration.password.required}")
        String password
){}
