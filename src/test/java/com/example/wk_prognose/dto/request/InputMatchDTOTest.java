package com.example.wk_prognose.dto.request;

import com.example.wk_prognose.repository.MatchRepository;
import com.example.wk_prognose.util.Country;
import com.example.wk_prognose.util.MatchStage;
import com.example.wk_prognose.validator.UniqueMatchSlotValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InputMatchDTOTest {

    private Validator validator;
    private MatchRepository matchRepository;

    @BeforeEach
    void beforeEach() {
        matchRepository = mock(MatchRepository.class);
        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .constraintValidatorFactory(new TestConstraintValidatorFactory(matchRepository))
                .buildValidatorFactory();
        validator = factory.getValidator();
    }

    private static Stream<Arguments> validMatchData() {
        return Stream.of(
                Arguments.of(validMatchDTO(null)),
                Arguments.of(new InputMatchDTO(1L, Country.BELGIUM, Country.FRANCE, validDate(),
                        1L, 97, 0, MatchStage.GROUP_A, 2, 1))
        );
    }

    @ParameterizedTest
    @MethodSource("validMatchData")
    void testValidMatch(InputMatchDTO validMatch) {
        Set<ConstraintViolation<InputMatchDTO>> violations = validator.validate(validMatch);
        assertThat(violations).isEmpty();
    }

    private static Stream<Arguments> invalidMatchData() {
        return Stream.of(
                Arguments.of(new InputMatchDTO(null, null, Country.FRANCE, validDate(), 1L, 97, 0, MatchStage.GROUP_A, null, null), "homeCountry"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, null, validDate(), 1L, 97, 0, MatchStage.GROUP_A, null, null), "awayCountry"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, null, 1L, 97, 0, MatchStage.GROUP_A, null, null), "matchDateTime"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, validDate(), null, 97, 0, MatchStage.GROUP_A, null, null), "locationId"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, validDate(), 0L, 97, 0, MatchStage.GROUP_A, null, null), "locationId"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, validDate(), 1L, null, 0, MatchStage.GROUP_A, null, null), "stadiumCode"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, validDate(), 1L, 0, 0, MatchStage.GROUP_A, null, null), "stadiumCode"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, validDate(), 1L, 97, null, MatchStage.GROUP_A, null, null), "checksum"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, validDate(), 1L, 97, -1, MatchStage.GROUP_A, null, null), "checksum"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, validDate(), 1L, 97, 0, null, null, null), "matchStage"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, validDate(), 1L, 97, 0, MatchStage.GROUP_A, -1, null), "homeScore"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, validDate(), 1L, 97, 0, MatchStage.GROUP_A, null, -1), "awayScore"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.BELGIUM, validDate(), 1L, 97, 0, MatchStage.GROUP_A, null, null), "awayCountry"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, LocalDateTime.of(2026, 6, 10, 23, 59), 1L, 97, 0, MatchStage.GROUP_A, null, null), "matchDateTime"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, LocalDateTime.of(2026, 7, 20, 0, 0), 1L, 97, 0, MatchStage.GROUP_A, null, null), "matchDateTime"),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, validDate(), 1L, 97, 1, MatchStage.GROUP_A, null, null), "checksum")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidMatchData")
    void testInvalidMatch(InputMatchDTO invalidMatch, String expected) {
        Set<ConstraintViolation<InputMatchDTO>> violations = validator.validate(invalidMatch);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals(expected));
    }

    @ParameterizedTest
    @MethodSource("uniqueSlotData")
    void testUniqueMatchSlot(InputMatchDTO inputMatchDTO, String expected) {
        if (inputMatchDTO.id() == null) {
            when(matchRepository.existsByLocationIdAndMatchDateTime(
                    inputMatchDTO.locationId(), inputMatchDTO.matchDateTime())).thenReturn(true);
        } else {
            when(matchRepository.existsByLocationIdAndMatchDateTimeAndIdNot(
                    inputMatchDTO.locationId(), inputMatchDTO.matchDateTime(), inputMatchDTO.id())).thenReturn(true);
        }

        Set<ConstraintViolation<InputMatchDTO>> violations = validator.validate(inputMatchDTO);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals(expected));
    }

    private static Stream<Arguments> uniqueSlotData() {
        return Stream.of(
                Arguments.of(validMatchDTO(null), "matchDateTime"),
                Arguments.of(validMatchDTO(null), "locationId"),
                Arguments.of(validMatchDTO(1L), "matchDateTime"),
                Arguments.of(validMatchDTO(1L), "locationId")
        );
    }

    private static InputMatchDTO validMatchDTO(Long id) {
        return new InputMatchDTO(
                id,
                Country.BELGIUM,
                Country.FRANCE,
                validDate(),
                1L,
                97,
                0,
                MatchStage.GROUP_A,
                null,
                null
        );
    }

    private static LocalDateTime validDate() {
        return LocalDateTime.of(2026, 6, 12, 20, 0);
    }

    private static class TestConstraintValidatorFactory implements ConstraintValidatorFactory {

        private final MatchRepository matchRepository;

        private TestConstraintValidatorFactory(MatchRepository matchRepository) {
            this.matchRepository = matchRepository;
        }

        @Override
        public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
            if (key == UniqueMatchSlotValidator.class) {
                return key.cast(new UniqueMatchSlotValidator(matchRepository));
            }

            try {
                return key.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public void releaseInstance(ConstraintValidator<?, ?> instance) {
        }
    }
}
