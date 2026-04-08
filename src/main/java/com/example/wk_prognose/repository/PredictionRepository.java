package com.example.wk_prognose.repository;

import com.example.wk_prognose.model.Match;
import com.example.wk_prognose.model.Prediction;
import com.example.wk_prognose.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PredictionRepository extends JpaRepository<Prediction, Long> {

    Optional<Prediction> findByUserAndMatch(User user, Match match);
}
