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

class MatchDateRangeValidatorTest {

    private MatchDateRangeValidator validator;

    @BeforeEach
    void beforeEach() {
        validator = new MatchDateRangeValidator();
    }

    @Test
    void testValidMatchDateRange() {
        assertThat(validator.isValid(matchDTO(LocalDateTime.of(2026, 6, 11, 0, 0)), null)).isTrue();
        assertThat(validator.isValid(matchDTO(LocalDateTime.of(2026, 7, 19, 23, 59)), null)).isTrue();
    }

    @Test
    void testInvalidMatchDateRange() {
        ConstraintValidatorContext context = context();

        assertThat(validator.isValid(matchDTO(LocalDateTime.of(2026, 6, 10, 23, 59)), context)).isFalse();
        assertThat(validator.isValid(matchDTO(LocalDateTime.of(2026, 7, 20, 0, 0)), context)).isFalse();
    }

    @Test
    void testNullValuesAreValid() {
        assertThat(validator.isValid(null, null)).isTrue();
        assertThat(validator.isValid(matchDTO(null), null)).isTrue();
    }

    private InputMatchDTO matchDTO(LocalDateTime matchDateTime) {
        return new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE,
                matchDateTime, 1L, 97, 0, MatchStage.GROUP_A, null, null);
    }

    private ConstraintValidatorContext context() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class, RETURNS_DEEP_STUBS);
        when(context.getDefaultConstraintMessageTemplate()).thenReturn("message");
        return context;
    }
}
