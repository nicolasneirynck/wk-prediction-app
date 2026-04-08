package com.example.wk_prognose.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = UniqueMatchSlotValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface ValidUniqueMatchSlot {

    String message() default "A match already exists for this location at the selected date and time.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
