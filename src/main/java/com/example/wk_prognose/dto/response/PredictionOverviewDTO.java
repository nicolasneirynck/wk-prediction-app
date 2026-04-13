package com.example.wk_prognose.dto.response;

import com.example.wk_prognose.util.MatchStage;
import java.time.LocalDateTime;

public record PredictionOverviewDTO(
        Long matchId,
        String homeCountry,
        String homeCountryCode,
        String awayCountry,
        String awayCountryCode,
        MatchStage matchStage,
        LocalDateTime matchDateTime,
        Integer predictedHomeScore,
        Integer predictedAwayScore,
        Integer finalHomeScore,
        Integer finalAwayScore,
        Integer basePointsEarned,
        Integer bonusPointsEarned,
        Integer pointsEarned
        ) {
}
