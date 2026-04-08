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

        @NotNull
        Country homeCountry,

        @NotNull
        Country awayCountry,

        @NotNull
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime matchDateTime,

        @NotNull
        @Min(1)
        Long locationId,

        @NotNull
        @Min(1)
        Integer stadiumCode,

        @NotNull
        @Min(0)
        Integer checksum,

        @NotNull
        MatchStage matchStage,

        @Min(0)
        Integer homeScore,

        @Min(0)
        Integer awayScore) {
}
