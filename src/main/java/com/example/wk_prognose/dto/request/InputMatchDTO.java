package com.example.wk_prognose.dto.request;

import com.example.wk_prognose.util.Country;
import com.example.wk_prognose.util.MatchStage;
import com.example.wk_prognose.validator.ValidDifferentTeams;
import com.example.wk_prognose.validator.ValidMatchDateRange;
import com.example.wk_prognose.validator.ValidStadiumChecksum;
import com.example.wk_prognose.validator.ValidUniqueMatchSlot;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@ValidStadiumChecksum
@ValidDifferentTeams
@ValidMatchDateRange
@ValidUniqueMatchSlot
public record InputMatchDTO(
        Long id,

        @NotNull(message = "{validation.match.homeCountry.required}")
        Country homeCountry,

        @NotNull(message = "{validation.match.awayCountry.required}")
        Country awayCountry,

        @NotNull(message = "{validation.match.matchDateTime.required}")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime matchDateTime,

        @NotNull(message = "{validation.match.locationId.required}")
        @Min(value = 1, message = "{validation.match.locationId.min}")
        Long locationId,

        @NotNull(message = "{validation.match.stadiumCode.required}")
        @Min(value = 1, message = "{validation.match.stadiumCode.min}")
        Integer stadiumCode,

        @NotNull(message = "{validation.match.checksum.required}")
        @Min(value = 0, message = "{validation.match.checksum.min}")
        Integer checksum,

        @NotNull(message = "{validation.match.matchStage.required}")
        MatchStage matchStage,

        @Min(value = 0, message = "{validation.match.homeScore.min}")
        Integer homeScore,

        @Min(value = 0, message = "{validation.match.awayScore.min}")
        Integer awayScore) {
}
