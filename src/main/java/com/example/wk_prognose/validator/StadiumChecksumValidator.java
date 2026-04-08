package com.example.wk_prognose.validator;

import com.example.wk_prognose.dto.request.InputMatchDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StadiumChecksumValidator implements ConstraintValidator<ValidStadiumChecksum, InputMatchDTO> {
    @Override
    public void initialize(ValidStadiumChecksum constraintAnnotation) {}

    @Override
    public boolean isValid(InputMatchDTO inputMatchDTO, ConstraintValidatorContext context) {

        if (inputMatchDTO == null || inputMatchDTO.stadiumCode() == null ||
            inputMatchDTO.checksum() == null)
            return true;

        if (inputMatchDTO.stadiumCode() % 97 == inputMatchDTO.checksum())
            return true;


        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("checksum")
                .addConstraintViolation();

        return false;
    }
}
