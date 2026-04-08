package com.example.wk_prognose.dto.response;

import com.example.wk_prognose.util.Country;
import com.example.wk_prognose.util.MatchStage;
import com.example.wk_prognose.util.MatchStatus;

import java.time.LocalDateTime;

public record MatchDTO(Long id,
                       String homeCountry,
                       String homeCountryCode,
                       Country homeCountryEnum,
                       String awayCountry,
                       String awayCountryCode,
                       Country awayCountryEnum,
                       LocalDateTime matchDateTime,
                       Long locationId,
                       String city,
                       String hostCountry,
                       String stadiumName,
                       MatchStage matchStage,
                       Integer stadiumCode,
                       Integer checksum,
                       Integer finalHomeScore,
                       Integer finalAwayScore,
                       MatchStatus matchStatus) {
}
