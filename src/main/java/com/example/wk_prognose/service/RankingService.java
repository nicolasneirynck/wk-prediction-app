package com.example.wk_prognose.service;

import com.example.wk_prognose.dto.response.TeamMemberDTO;
import com.example.wk_prognose.dto.response.TeamRankingDTO;
import com.example.wk_prognose.model.Team;
import com.example.wk_prognose.model.User;
import com.example.wk_prognose.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final PredictionScoreService predictionScoreService;
    private final TeamRepository teamRepository;

    public List<TeamMemberDTO> findRankedMembersForTeam(Long teamId){

        Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new IllegalArgumentException("No team found"));

        User owner = team.getOwner();

        return team.getUsers().stream()
                .map(user -> toTeamMemberDTO(user, owner))
                .sorted((member1, member2) -> member2.score() - member1.score())
                .toList();
    }

    public int calculateTotalScoreForTeam(Long teamId){
        return findRankedMembersForTeam(teamId).stream().mapToInt(TeamMemberDTO::score).sum();
    }

    public List<TeamRankingDTO> findTop10Teams(){
        List<Team> allTeams = teamRepository.findAll();

        return allTeams.stream()
                .map(this::toTeamRankingDTO)
                .sorted((team1, team2) -> team2.totalScore() - team1.totalScore())
                .limit(10)
                .toList();

    }


    private TeamMemberDTO toTeamMemberDTO(User user, User teamOwner){

        String displayName = user.getFirstName() + " " + user.getLastName();
        boolean isOwner = user.getId() == teamOwner.getId();

        int score = calculateTotalScoreForUser(user);

        return new TeamMemberDTO(user.getId(), displayName, score, isOwner);
    }

    private TeamRankingDTO toTeamRankingDTO(Team team){
        int memberCount = team.getUsers().size();
        int totalScore = calculateTotalScoreForTeam(team.getId());

        return new TeamRankingDTO(team.getId(), team.getName(), totalScore, memberCount);
    }

    private int calculateTotalScoreForUser(User user){
        return user.getPredictions().stream().mapToInt(prediction -> {
            int baseScore = predictionScoreService.calculateBaseScore(prediction);
            int bonusScore = predictionScoreService.calculateBonusScore(prediction);

            return baseScore + bonusScore;
        }).sum();
    }
}
