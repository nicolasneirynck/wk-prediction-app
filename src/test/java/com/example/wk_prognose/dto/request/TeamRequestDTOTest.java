package com.example.wk_prognose.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TeamRequestDTOTest {

    private Validator validator;

    @BeforeEach
    void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidCreateTeam() {
        CreateTeamDTO createTeamDTO = new CreateTeamDTO("Team Test");

        Set<ConstraintViolation<CreateTeamDTO>> violations = validator.validate(createTeamDTO);
        assertThat(violations).isEmpty();
    }

    @Test
    void testInvalidCreateTeam() {
        CreateTeamDTO createTeamDTO = new CreateTeamDTO("");

        Set<ConstraintViolation<CreateTeamDTO>> violations = validator.validate(createTeamDTO);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    void testValidJoinTeam() {
        JoinTeamDTO joinTeamDTO = new JoinTeamDTO("ABC12345");

        Set<ConstraintViolation<JoinTeamDTO>> violations = validator.validate(joinTeamDTO);
        assertThat(violations).isEmpty();
    }

    @Test
    void testInvalidJoinTeam() {
        JoinTeamDTO joinTeamDTO = new JoinTeamDTO("");

        Set<ConstraintViolation<JoinTeamDTO>> violations = validator.validate(joinTeamDTO);
        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("inviteCode"));
    }
}
