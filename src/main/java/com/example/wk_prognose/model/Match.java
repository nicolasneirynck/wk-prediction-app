package com.example.wk_prognose.model;

import com.example.wk_prognose.util.Country;
import com.example.wk_prognose.util.MatchStage;
import com.example.wk_prognose.util.MatchStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "matches")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter(AccessLevel.PRIVATE)
    @Enumerated(EnumType.STRING)
    private Country homeCountry;

    @Setter(AccessLevel.PRIVATE)
    @Enumerated(EnumType.STRING)
    private Country awayCountry;

    @Setter(AccessLevel.PRIVATE)
    private LocalDateTime matchDateTime;

    @Setter(AccessLevel.PRIVATE)
    @ManyToOne
    private Location location;

    @Setter(AccessLevel.PRIVATE)
    private int stadiumCode;
    @Setter(AccessLevel.PRIVATE)
    private int checksum;

    private Integer finalHomeScore;
    private Integer finalAwayScore;

    @Setter(AccessLevel.PRIVATE)
    @Enumerated(EnumType.STRING)
    private MatchStage matchStage;

    @OneToMany(mappedBy = "match")
    private List<Prediction> predictions;

    public Match(Country homeCountry, Country awayCountry, LocalDateTime matchDateTime, Location location,
                 int stadiumCode, int checksum, MatchStage matchStage){
        this.homeCountry = homeCountry;
        this.awayCountry = awayCountry;
        this.matchDateTime = matchDateTime;
        this.location = location;
        this.stadiumCode = stadiumCode;
        this.checksum = checksum;
        this.matchStage = matchStage;
    }

    public void update(Country homeCountry, Country awayCountry, LocalDateTime matchDateTime, Location location,
                            int stadiumCode, int checksum, MatchStage matchStage) {
        setHomeCountry(homeCountry);
        setAwayCountry(awayCountry);
        setMatchDateTime(matchDateTime);
        setLocation(location);
        setStadiumCode(stadiumCode);
        setChecksum(checksum);
        setMatchStage(matchStage);
    }

    public MatchStatus getStatus() {
        if (finalHomeScore != null && finalAwayScore != null) {
            return MatchStatus.COMPLETED;
        }

        if (matchDateTime.isAfter(LocalDateTime.now())) {
            return MatchStatus.UPCOMING;
        }

        return MatchStatus.AWAITING_RESULT;
    }

    public void updateFinalScore(Integer homeScore, Integer awayScore){
        setFinalHomeScore(homeScore);
        setFinalAwayScore(awayScore);
    }

    private void setFinalHomeScore(Integer homeScore){

        if (homeScore == null)
            throw new IllegalArgumentException("Home score is required");

        if (homeScore < 0)
            throw new IllegalArgumentException("Home score cannot be negative");

        this.finalHomeScore = homeScore;
    }

    private void setFinalAwayScore(Integer awayScore){

        if (awayScore == null)
            throw new IllegalArgumentException("Away score is required");

        if (awayScore < 0)
            throw new IllegalArgumentException("Away score cannot be negative.");

        this.finalAwayScore = awayScore;
    }

}
