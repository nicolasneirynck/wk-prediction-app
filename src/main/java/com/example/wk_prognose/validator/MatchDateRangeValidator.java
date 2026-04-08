package com.example.wk_prognose.validator;

import com.example.wk_prognose.dto.request.InputMatchDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class MatchDateRangeValidator implements ConstraintValidator<ValidMatchDateRange, InputMatchDTO> {
    private static final LocalDateTime EARLIEST_MATCH_DATE_TIME = LocalDateTime.of(2026, 6, 11, 0, 0);
    private static final LocalDateTime LATEST_MATCH_DATE_TIME_EXCLUSIVE = LocalDateTime.of(2026, 7, 20, 0, 0);

    @Override
    public boolean isValid(InputMatchDTO inputMatchDTO, ConstraintValidatorContext context) {

        if (inputMatchDTO == null || inputMatchDTO.matchDateTime() == null)
            return true;

        LocalDateTime matchDateTime = inputMatchDTO.matchDateTime();
        boolean isInRange = !matchDateTime.isBefore(EARLIEST_MATCH_DATE_TIME)
                && matchDateTime.isBefore(LATEST_MATCH_DATE_TIME_EXCLUSIVE);

        if (isInRange)
            return true;

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("matchDateTime")
                .addConstraintViolation();

        return false;
    }
}
