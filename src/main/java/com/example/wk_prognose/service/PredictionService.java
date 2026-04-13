package com.example.wk_prognose.service;

import com.example.wk_prognose.dto.request.InputPredictionDTO;
import com.example.wk_prognose.dto.response.PredictionDTO;
import com.example.wk_prognose.dto.response.PredictionOverviewDTO;
import com.example.wk_prognose.model.Match;
import com.example.wk_prognose.model.Prediction;
import com.example.wk_prognose.model.User;
import com.example.wk_prognose.repository.MatchRepository;
import com.example.wk_prognose.repository.PredictionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final PredictionRepository predictionRepository;
    private final CurrentUserService currentUserService;
    private final MatchRepository matchRepository;
    private final PredictionScoreService predictionScoreService;

    public Optional<PredictionDTO> findPredictionByMatchId(Long matchId){
        User currentUser = currentUserService.getCurrentUser().orElseThrow(() -> new IllegalStateException("Current user not found"));
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new IllegalArgumentException("Match not found"));

        return predictionRepository.findByUserAndMatch(currentUser, match).map(this::toDTO);
    }

    public void savePrediction(Long matchId, InputPredictionDTO inputPredictionDTO){
        User currentUser = currentUserService.getCurrentUser().orElseThrow(() -> new IllegalStateException("Current user not found"));
        Match match = matchRepository.findById(matchId).orElseThrow(() -> new IllegalStateException("Match not found"));

        validatePredictionDeadline(match);

        Prediction prediction = predictionRepository.findByUserAndMatch(currentUser,match)
                .orElse(new Prediction(currentUser,match));

        prediction.updatePrediction(inputPredictionDTO.predictedHomeScore(),
                inputPredictionDTO.predictedAwayScore());

        predictionRepository.save(prediction);
    }

    public List<Prediction> findPredictionsForCurrentUser(){
        User currentUser = currentUserService.getCurrentUser().orElseThrow(() -> new IllegalStateException("Current user not found"));

        return predictionRepository.findByUser(currentUser);
    }

    public int calculateTotalScoreForCurrentUser() {
        User currentUser = currentUserService.getCurrentUser().orElseThrow(() -> new IllegalStateException("Current user not found"));
        return calculateTotalScoreForUser(currentUser);
    }

    public int calculateTotalScoreForUser(User user) {
        return predictionRepository.findByUser(user).stream()
                .mapToInt(this::calculatePointsEarned)
                .sum();
    }

    public List<PredictionOverviewDTO> findUpcomingPredictionsForCurrentUser(){
        List<Prediction> predictionsUser = this.findPredictionsForCurrentUser();

        return predictionsUser.stream().filter(prediction -> {
            Match match = prediction.getMatch();
            boolean finalScoresAvailable = match.getFinalHomeScore() != null && match.getFinalAwayScore() != null;

            return !finalScoresAvailable;
        }).map(this::toOverviewDTO).toList();
    }

    public List<PredictionOverviewDTO> findPastPredictionsForCurrentUser(){
        List<Prediction> predictionsUser = this.findPredictionsForCurrentUser();

        return predictionsUser.stream().filter(prediction -> {
            Match match = prediction.getMatch();
            return match.getFinalHomeScore() != null && match.getFinalAwayScore() != null;
        }).map(this::toOverviewDTO).toList();
    }

    private PredictionDTO toDTO(Prediction prediction){
        return new PredictionDTO(
                prediction.getMatch().getId(),
                prediction.getPredictedHomeScore(),
                prediction.getPredictedAwayScore()
        );
    }

    private PredictionOverviewDTO toOverviewDTO(Prediction prediction){

        Match match = prediction.getMatch();
        int basePointsEarned = predictionScoreService.calculateBaseScore(prediction);
        int bonusPointsEarned = predictionScoreService.calculateBonusScore(prediction);
        int pointsEarned = basePointsEarned + bonusPointsEarned;

        return new PredictionOverviewDTO(
                match.getId(),
                match.getHomeCountry().getDisplayName(),
                match.getHomeCountry().getIsoCode(),
                match.getAwayCountry().getDisplayName(),
                match.getAwayCountry().getIsoCode(),
                match.getMatchStage(),
                match.getMatchDateTime(),
                prediction.getPredictedHomeScore(), prediction.getPredictedAwayScore(), match.getFinalHomeScore(),
                match.getFinalAwayScore(), basePointsEarned, bonusPointsEarned, pointsEarned
        );
    }

    private int calculatePointsEarned(Prediction prediction) {
        return predictionScoreService.calculateBaseScore(prediction)
                + predictionScoreService.calculateBonusScore(prediction);
    }

    private void validatePredictionDeadline(Match match){

        LocalDateTime deadline = match.getMatchDateTime().minusHours(1);

        if (LocalDateTime.now().isAfter(deadline))
            throw new IllegalArgumentException("The deadline is until one hour before kickoff");
    }
}
