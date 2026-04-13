package com.example.wk_prognose.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "score")
public record ScoreProperties(
        int exactScore,
        int correctOutcome,
        int exactWithinTeam,
        int outcomeWithinTeam
) {
}
