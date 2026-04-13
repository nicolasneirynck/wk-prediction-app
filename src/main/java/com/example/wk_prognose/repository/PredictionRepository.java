package com.example.wk_prognose.repository;

import com.example.wk_prognose.model.Match;
import com.example.wk_prognose.model.Prediction;
import com.example.wk_prognose.model.Team;
import com.example.wk_prognose.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    Optional<Prediction> findByUserAndMatch(User user, Match match);

    List<Prediction> findByUser(User user);

    List<Prediction> findAllByMatchAndUser_Team(Match match, Team team);
    /*
    geef alle Prediction-records terug waarvoor:
    - de match gelijk is aan de meegegeven match
    - en de team van de gekoppelde user gelijk is aan het meegegeven team”
     */
}
