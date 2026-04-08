package com.example.wk_prognose.validator;

import com.example.wk_prognose.dto.request.InputMatchDTO;
import com.example.wk_prognose.repository.MatchRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueMatchSlotValidator implements ConstraintValidator<ValidUniqueMatchSlot, InputMatchDTO> {

    private final MatchRepository matchRepository;

    @Override
    public boolean isValid(InputMatchDTO inputMatchDTO, ConstraintValidatorContext context) {

        if (inputMatchDTO == null || inputMatchDTO.locationId() == null || inputMatchDTO.matchDateTime() == null) {
            return true;
        }

        boolean conflict = inputMatchDTO.id() == null
                ? matchRepository.existsByLocationIdAndMatchDateTime(
                inputMatchDTO.locationId(),
                inputMatchDTO.matchDateTime())
                : matchRepository.existsByLocationIdAndMatchDateTimeAndIdNot(
                inputMatchDTO.locationId(),
                inputMatchDTO.matchDateTime(),
                inputMatchDTO.id());

        if (!conflict) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("matchDateTime")
                .addConstraintViolation();
        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode("locationId")
                .addConstraintViolation();

        return false;
    }
}
