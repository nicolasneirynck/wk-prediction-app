package com.example.wk_prognose.service;

import com.example.wk_prognose.config.ScoreProperties;
import com.example.wk_prognose.model.Match;
import com.example.wk_prognose.model.Prediction;
import com.example.wk_prognose.model.Team;
import com.example.wk_prognose.repository.PredictionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredictionScoreService {

    private final ScoreProperties scoreProperties;
    private final PredictionRepository predictionRepository;

    public PredictionScoreService(ScoreProperties scoreProperties, PredictionRepository predictionRepository) {
        this.scoreProperties = scoreProperties;
        this.predictionRepository = predictionRepository;
    }

    public int calculateBaseScore(Prediction prediction) {
        int predictedHome = prediction.getPredictedHomeScore();
        int predictedAway = prediction.getPredictedAwayScore();
        Integer finalHome = prediction.getMatch().getFinalHomeScore();
        Integer finalAway = prediction.getMatch().getFinalAwayScore();

        if (finalHome == null || finalAway == null) {
            return 0;
        }

        if (predictedHome == finalHome && predictedAway == finalAway) {
            return scoreProperties.exactScore();
        }

        int predictedOutcome = Integer.compare(predictedHome, predictedAway);
        int finalOutcome = Integer.compare(finalHome, finalAway);

        if (predictedOutcome == finalOutcome) {
            return scoreProperties.correctOutcome();
        }

        return 0;
    }

    public int calculateBonusScore(Prediction prediction) {
        List<Prediction> teamPredictions = findTeamPredictionsForSameMatch(prediction);

        if (hasExactScore(prediction)) {
            long exactScoreCount = teamPredictions.stream()
                    .filter(this::hasExactScore)
                    .count();

            if (exactScoreCount == 1) {
                return scoreProperties.exactWithinTeam();
            }
        }

        if (hasCorrectOutcome(prediction)) {
            long correctOutcomeCount = teamPredictions.stream()
                    .filter(this::hasCorrectOutcome)
                    .count();

            if (correctOutcomeCount == 1) {
                return scoreProperties.outcomeWithinTeam();
            }
        }

        return 0;
    }

    private List<Prediction> findTeamPredictionsForSameMatch(Prediction prediction) {
        Match match = prediction.getMatch();
        Team team = prediction.getUser().getTeam();

        if (team == null) {
            return List.of();
        }

        return predictionRepository.findAllByMatchAndUser_Team(match, team);
    }

    private boolean hasExactScore(Prediction prediction) {
        Integer finalHome = prediction.getMatch().getFinalHomeScore();
        Integer finalAway = prediction.getMatch().getFinalAwayScore();

        if (finalHome == null || finalAway == null) {
            return false;
        }

        return prediction.getPredictedHomeScore() == finalHome
                && prediction.getPredictedAwayScore() == finalAway;
    }

    private boolean hasCorrectOutcome(Prediction prediction) {
        Integer finalHome = prediction.getMatch().getFinalHomeScore();
        Integer finalAway = prediction.getMatch().getFinalAwayScore();

        if (finalHome == null || finalAway == null) {
            return false;
        }

        int predictedOutcome = Integer.compare(
                prediction.getPredictedHomeScore(),
                prediction.getPredictedAwayScore()
        );
        int finalOutcome = Integer.compare(finalHome, finalAway);

        return predictedOutcome == finalOutcome;
    }
}
