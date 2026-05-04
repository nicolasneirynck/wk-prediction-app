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

class InputPredictionDTOTest {

    private Validator validator;

    @BeforeEach
    void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private static Stream<Arguments> validPredictionData() {
        return Stream.of(
                Arguments.of(0, 0),
                Arguments.of(2, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("validPredictionData")
    void testValidPrediction(Integer homeScore, Integer awayScore) {
        InputPredictionDTO validPrediction = new InputPredictionDTO(homeScore, awayScore);

        Set<ConstraintViolation<InputPredictionDTO>> violations = validator.validate(validPrediction);
        assertThat(violations).isEmpty();
    }

    private static Stream<Arguments> invalidPredictionData() {
        return Stream.of(
                Arguments.of(null, 1, "predictedHomeScore"),
                Arguments.of(-1, 1, "predictedHomeScore"),
                Arguments.of(1, null, "predictedAwayScore"),
                Arguments.of(1, -1, "predictedAwayScore")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPredictionData")
    void testInvalidPrediction(Integer homeScore, Integer awayScore, String expected) {
        InputPredictionDTO invalidPrediction = new InputPredictionDTO(homeScore, awayScore);

        Set<ConstraintViolation<InputPredictionDTO>> violations = validator.validate(invalidPrediction);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals(expected));
    }
}
