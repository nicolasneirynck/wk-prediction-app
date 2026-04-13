package com.example.wk_prognose.dto.response;

public record PredictionDTO(Long matchId,
                            Integer predictedHomeScore,
                            Integer predictedAwayScore) {
}
