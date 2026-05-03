package com.example.wk_prognose.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DifferentTeamsValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface ValidDifferentTeams {

    String message() default "{validation.match.differentTeams}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
