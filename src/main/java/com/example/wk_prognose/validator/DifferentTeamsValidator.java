package com.example.wk_prognose.validator;

import com.example.wk_prognose.dto.request.InputMatchDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DifferentTeamsValidator implements ConstraintValidator<ValidDifferentTeams, InputMatchDTO> {
    @Override
    public boolean isValid(InputMatchDTO inputMatchDTO, ConstraintValidatorContext context) {

        if (inputMatchDTO == null || inputMatchDTO.homeCountry() == null ||
            inputMatchDTO.awayCountry() == null)
            return true;

        if (!inputMatchDTO.homeCountry().equals(inputMatchDTO.awayCountry()))
            return true;

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("awayCountry")
                .addConstraintViolation();

        return false;
    }
}
