package com.example.wk_prognose.repository;

import com.example.wk_prognose.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByInviteCode(String inviteCode);
    boolean existsByName(String name);
}
