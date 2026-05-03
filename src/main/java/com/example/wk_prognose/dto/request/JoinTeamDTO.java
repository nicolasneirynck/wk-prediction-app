package com.example.wk_prognose.dto.request;

import jakarta.validation.constraints.NotBlank;

public record JoinTeamDTO(
        @NotBlank(message = "{validation.team.inviteCode.required}")
        String inviteCode) {
}
