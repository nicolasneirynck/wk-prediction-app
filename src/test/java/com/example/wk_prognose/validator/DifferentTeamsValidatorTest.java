package com.example.wk_prognose.validator;

import com.example.wk_prognose.dto.request.InputMatchDTO;
import com.example.wk_prognose.util.Country;
import com.example.wk_prognose.util.MatchStage;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DifferentTeamsValidatorTest {

    private DifferentTeamsValidator validator;

    @BeforeEach
    void beforeEach() {
        validator = new DifferentTeamsValidator();
    }

    @Test
    void testValidDifferentTeams() {
        InputMatchDTO inputMatchDTO = matchDTO(Country.BELGIUM, Country.FRANCE);

        assertThat(validator.isValid(inputMatchDTO, null)).isTrue();
    }

    @Test
    void testInvalidDifferentTeams() {
        InputMatchDTO inputMatchDTO = matchDTO(Country.BELGIUM, Country.BELGIUM);
        ConstraintValidatorContext context = context();

        assertThat(validator.isValid(inputMatchDTO, context)).isFalse();
    }

    @Test
    void testNullValuesAreValid() {
        assertThat(validator.isValid(null, null)).isTrue();
        assertThat(validator.isValid(matchDTO(null, Country.FRANCE), null)).isTrue();
        assertThat(validator.isValid(matchDTO(Country.BELGIUM, null), null)).isTrue();
    }

    private InputMatchDTO matchDTO(Country homeCountry, Country awayCountry) {
        return new InputMatchDTO(null, homeCountry, awayCountry,
                LocalDateTime.of(2026, 6, 12, 20, 0), 1L,
                97, 0, MatchStage.GROUP_A, null, null);
    }

    private ConstraintValidatorContext context() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class, RETURNS_DEEP_STUBS);
        when(context.getDefaultConstraintMessageTemplate()).thenReturn("message");
        return context;
    }
}
