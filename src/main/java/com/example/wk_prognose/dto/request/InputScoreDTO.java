package com.example.wk_prognose.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InputScoreDTO(
        @NotNull(message = "{validation.score.homeScore.required}")
        @Min(value = 0, message = "{validation.score.homeScore.min}")
        Integer homeScore,

        @NotNull(message = "{validation.score.awayScore.required}")
        @Min(value = 0, message = "{validation.score.awayScore.min}")
        Integer awayScore) {
}
