package com.example.wk_prognose.repository;

import com.example.wk_prognose.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findAllByOrderByMatchDateTimeAsc();
    boolean existsByLocationIdAndMatchDateTime(Long locationId, LocalDateTime matchDateTime);
    boolean existsByLocationIdAndMatchDateTimeAndIdNot(Long locationId, LocalDateTime matchDateTime, Long id);
}
