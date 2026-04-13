package com.example.wk_prognose.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InputPredictionDTO(
        @NotNull
        @Min(0)
        Integer predictedHomeScore,

        @NotNull
        @Min(0)
        Integer predictedAwayScore) {
}
