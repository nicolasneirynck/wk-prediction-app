package com.example.wk_prognose.validator;

import com.example.wk_prognose.dto.request.InputMatchDTO;
import com.example.wk_prognose.repository.MatchRepository;
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

class UniqueMatchSlotValidatorTest {

    private MatchRepository matchRepository;
    private UniqueMatchSlotValidator validator;

    @BeforeEach
    void beforeEach() {
        matchRepository = mock(MatchRepository.class);
        validator = new UniqueMatchSlotValidator(matchRepository);
    }

    @Test
    void testValidUniqueSlotForNewMatch() {
        InputMatchDTO inputMatchDTO = matchDTO(null);
        when(matchRepository.existsByLocationIdAndMatchDateTime(
                inputMatchDTO.locationId(), inputMatchDTO.matchDateTime())).thenReturn(false);

        assertThat(validator.isValid(inputMatchDTO, null)).isTrue();
    }

    @Test
    void testInvalidUniqueSlotForNewMatch() {
        InputMatchDTO inputMatchDTO = matchDTO(null);
        ConstraintValidatorContext context = context();
        when(matchRepository.existsByLocationIdAndMatchDateTime(
                inputMatchDTO.locationId(), inputMatchDTO.matchDateTime())).thenReturn(true);

        assertThat(validator.isValid(inputMatchDTO, context)).isFalse();
    }

    @Test
    void testValidUniqueSlotForExistingMatch() {
        InputMatchDTO inputMatchDTO = matchDTO(1L);
        when(matchRepository.existsByLocationIdAndMatchDateTimeAndIdNot(
                inputMatchDTO.locationId(), inputMatchDTO.matchDateTime(), inputMatchDTO.id())).thenReturn(false);

        assertThat(validator.isValid(inputMatchDTO, null)).isTrue();
    }

    @Test
    void testInvalidUniqueSlotForExistingMatch() {
        InputMatchDTO inputMatchDTO = matchDTO(1L);
        ConstraintValidatorContext context = context();
        when(matchRepository.existsByLocationIdAndMatchDateTimeAndIdNot(
                inputMatchDTO.locationId(), inputMatchDTO.matchDateTime(), inputMatchDTO.id())).thenReturn(true);

        assertThat(validator.isValid(inputMatchDTO, context)).isFalse();
    }

    @Test
    void testNullValuesAreValid() {
        assertThat(validator.isValid(null, null)).isTrue();
        assertThat(validator.isValid(matchDTOWithNullLocation(), null)).isTrue();
        assertThat(validator.isValid(matchDTOWithNullDate(), null)).isTrue();
    }

    private InputMatchDTO matchDTO(Long id) {
        return new InputMatchDTO(id, Country.BELGIUM, Country.FRANCE,
                LocalDateTime.of(2026, 6, 12, 20, 0), 1L,
                97, 0, MatchStage.GROUP_A, null, null);
    }

    private InputMatchDTO matchDTOWithNullLocation() {
        return new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE,
                LocalDateTime.of(2026, 6, 12, 20, 0), null,
                97, 0, MatchStage.GROUP_A, null, null);
    }

    private InputMatchDTO matchDTOWithNullDate() {
        return new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE,
                null, 1L, 97, 0, MatchStage.GROUP_A, null, null);
    }

    private ConstraintValidatorContext context() {
        ConstraintValidatorContext context = mock(ConstraintValidatorContext.class, RETURNS_DEEP_STUBS);
        when(context.getDefaultConstraintMessageTemplate()).thenReturn("message");
        return context;
    }
}
