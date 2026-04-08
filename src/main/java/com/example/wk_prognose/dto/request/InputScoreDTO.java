package com.example.wk_prognose.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InputScoreDTO(
        @NotNull
        @Min(0)
        Integer homeScore,

        @NotNull
        @Min(0)
        Integer awayScore) {
}
