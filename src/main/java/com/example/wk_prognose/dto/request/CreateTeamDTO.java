package com.example.wk_prognose.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateTeamDTO(
        @NotBlank(message = "{validation.team.name.required}")
        String name){

}
