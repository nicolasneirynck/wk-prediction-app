package com.example.wk_prognose.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = MatchDateRangeValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface ValidMatchDateRange {

    String message() default "Match date must be between 11 June 2026 and 19 July 2026.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
