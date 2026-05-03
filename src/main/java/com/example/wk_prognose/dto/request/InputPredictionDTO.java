package com.example.wk_prognose.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InputPredictionDTO(
        @NotNull(message = "{validation.prediction.homeScore.required}")
        @Min(value = 0, message = "{validation.prediction.homeScore.min}")
        Integer predictedHomeScore,

        @NotNull(message = "{validation.prediction.awayScore.required}")
        @Min(value = 0, message = "{validation.prediction.awayScore.min}")
        Integer predictedAwayScore) {
}
