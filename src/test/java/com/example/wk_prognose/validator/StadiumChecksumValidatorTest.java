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

class StadiumChecksumValidatorTest {

    private StadiumChecksumValidator validator;

    @BeforeEach
    void beforeEach() {
        validator = new StadiumChecksumValidator();
    }

    @Test
    void testValidStadiumChecksum() {
        InputMatchDTO inputMatchDTO = matchDTO(97, 0);

        assertThat(validator.isValid(inputMatchDTO, null)).isTrue();
    }

    @Test
    void testInvalidStadiumChecksum() {
        InputMatchDTO inputMatchDTO = matchDTO(97, 1);
        ConstraintValidatorContext context = context();

        assertThat(validator.isValid(inputMatchDTO, context)).isFalse();
    }

    @Test
    void testNullValuesAreValid() {
        assertThat(validator.isValid(null, null)).isTrue();
        assertThat(validator.isValid(matchDTO(null, 0), null)).isTrue();
        assertThat(validator.isValid(matchDTO(97, null), null)).isTrue();
    }

    private InputMatchDTO matchDTO(Integer stadiumCode, Integer checksum) {
        return new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE,
                LocalDateTime.of(2026, 6, 12, 20, 0), 1L,
                stadiumCode, checksum, MatchStage.GROUP_A, null, null);
    }

    private ConstraintValidatorContext context() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class, RETURNS_DEEP_STUBS);
        when(context.getDefaultConstraintMessageTemplate()).thenReturn("message");
        return context;
    }
}
