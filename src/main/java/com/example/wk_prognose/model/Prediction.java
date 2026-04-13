package com.example.wk_prognose.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.NONE)
    private long id;

    private int predictedHomeScore;
    private int predictedAwayScore;

    @ManyToOne(optional = false) // predictie MOET bij een user horen
    private User user;

    @ManyToOne(optional = false) // predictie MOET bij een match horen
    private Match match;

    public Prediction(User user, Match match){

        if (user == null)
            throw new IllegalArgumentException("No user linked to the prediction");

        if (match == null)
            throw new IllegalArgumentException("No match linked to the prediction");

        this.user = user;
        this.match = match;
    }

    private void setPredictedHomeScore(Integer predictedHomeScore){

        if (predictedHomeScore == null)
            throw new IllegalArgumentException("Home score is required");

        if (predictedHomeScore < 0)
            throw new IllegalArgumentException("Home score cannot be negative");

        this.predictedHomeScore = predictedHomeScore;
    }

    private void setPredictedAwayScore(Integer predictedAwayScore){

        if (predictedAwayScore == null)
            throw new IllegalArgumentException("Away score is required");

        if (predictedAwayScore < 0)
            throw new IllegalArgumentException("Away score cannot be negative");

        this.predictedAwayScore = predictedAwayScore;
    }

    public void updatePrediction(Integer predictedHomeScore, Integer predictedAwayScore){
        setPredictedHomeScore(predictedHomeScore);
        setPredictedAwayScore(predictedAwayScore);
    }
}
