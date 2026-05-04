package com.example.wk_prognose.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class InputRegistrationDTOTest {

    private Validator validator;

    @BeforeEach
    void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Stream<Arguments> validRegistrationData() {
        return Stream.of(
                Arguments.of("Nicolas", "Test", "nicolas@test.be", "secret"),
                Arguments.of("A", "B", "a@b.be", "x")
        );
    }

    @ParameterizedTest
    @MethodSource("validRegistrationData")
    void testValidRegistration(String firstName, String lastName, String email, String password) {
        InputRegistrationDTO validRegistration = new InputRegistrationDTO(firstName, lastName, email, password);

        Set<ConstraintViolation<InputRegistrationDTO>> violations = validator.validate(validRegistration);
        assertThat(violations).isEmpty();
    }

    private static Stream<Arguments> invalidRegistrationData() {
        return Stream.of(
                Arguments.of("", "Test", "nicolas@test.be", "secret", "firstName"),
                Arguments.of("   ", "Test", "nicolas@test.be", "secret", "firstName"),
                Arguments.of("Nicolas", "", "nicolas@test.be", "secret", "lastName"),
                Arguments.of("Nicolas", "Test", "", "secret", "email"),
                Arguments.of("Nicolas", "Test", "wrong", "secret", "email"),
                Arguments.of("Nicolas", "Test", "nicolas@test.be", "", "password")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidRegistrationData")
    void testInvalidRegistration(String firstName, String lastName, String email, String password, String expected) {
        InputRegistrationDTO invalidRegistration = new InputRegistrationDTO(firstName, lastName, email, password);

        Set<ConstraintViolation<InputRegistrationDTO>> violations = validator.validate(invalidRegistration);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals(expected));
    }
}
