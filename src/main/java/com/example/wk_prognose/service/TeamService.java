package com.example.wk_prognose.service;

import com.example.wk_prognose.dto.request.CreateTeamDTO;
import com.example.wk_prognose.dto.request.JoinTeamDTO;
import com.example.wk_prognose.dto.response.TeamDetailDTO;
import com.example.wk_prognose.dto.response.TeamMemberDTO;
import com.example.wk_prognose.model.Team;
import com.example.wk_prognose.model.User;
import com.example.wk_prognose.repository.TeamRepository;
import com.example.wk_prognose.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    private final PredictionService predictionService;

    public Optional<TeamDetailDTO> findTeamById(Long id){
        return teamRepository.findById(id).map(this::toTeamDetailDTO);
    }

    public String findMyTeamLink(){
        User user = currentUserService.getCurrentUser().orElseThrow(() -> new IllegalStateException("Current user not found"));

        if (user.getCreatedTeam() != null)
            return "/teams/" + user.getCreatedTeam().getId();

        if (user.getTeam() != null)
            return "/teams/" + user.getTeam().getId();

        return "/teams";
    }

    public boolean currentUserHasTeam() {
        User user = currentUserService.getCurrentUser().orElseThrow(() -> new IllegalStateException("Current user not found"));

        return user.getCreatedTeam() != null || user.getTeam() != null;
    }

    public void createTeam(CreateTeamDTO createTeamDTO){

        if (teamRepository.existsByName(createTeamDTO.name()))
            throw new IllegalArgumentException("This name has already been chosen");

        User owner = currentUserService.getCurrentUser().orElseThrow(() -> new IllegalStateException("Current user not found"));

        if (owner.getCreatedTeam() != null)
            throw new IllegalArgumentException("You can create only one team!");

        String inviteCode = generateInviteCode();
        Team team = new Team(createTeamDTO.name(), owner, inviteCode);
        owner.setTeam(team);

        teamRepository.save(team);
        userRepository.save(owner);
    }

    private String generateInviteCode() {
        String code;
        do {
            code = UUID.randomUUID()
                    .toString()
                    .replace("-", "")
                    .substring(0, 8)
                    .toUpperCase();
        } while (teamRepository.findByInviteCode(code).isPresent());

        return code;
    }

    public void joinTeam(JoinTeamDTO joinTeamDTO){

        User user = currentUserService.getCurrentUser().orElseThrow(() -> new IllegalStateException("Current user not found"));

        if (user.getTeam() != null)
            throw new IllegalArgumentException("You can be part of one team!");

        Team team = teamRepository.findByInviteCode(joinTeamDTO.inviteCode()).orElseThrow(() ->
                new IllegalArgumentException("This isn't a valid invite code"));

        user.setTeam(team);

        userRepository.save(user);
    }

    public void regenerateInviteCode(Long teamId){
        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new IllegalArgumentException("Team not found"));

        User user = currentUserService.getCurrentUser().orElseThrow(() -> new IllegalStateException("Current user not found"));

        if (user.getId() != team.getOwner().getId())
            throw new IllegalArgumentException("Only the owner of the team can regenerate invite code!");

        team.setInviteCode(generateInviteCode());
        teamRepository.save(team);
    }

    public void removeMember(Long teamId, Long userId){

        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new IllegalArgumentException("Team not found"));

        User currentUser = currentUserService.getCurrentUser().orElseThrow(() -> new IllegalStateException("Current user not found"));

        if (currentUser.getId() != team.getOwner().getId())
            throw new IllegalArgumentException("Only the owner of the team can remove members!");

        User member = userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("The user you want to delete doesn't exist!"));

        this.removeMember(team, member);
    }

    private TeamDetailDTO toTeamDetailDTO(Team team){

        User owner = team.getOwner();
        User currentUser = currentUserService.getCurrentUser().orElseThrow(() -> new IllegalStateException("Current user not found"));
        String ownerName = owner.getFirstName() + " " + owner.getLastName();
        int memberCount = team.getUsers().size();
        int totalScore = team.getUsers().stream()
                .mapToInt(predictionService::calculateTotalScoreForUser)
                .sum();
        List<TeamMemberDTO> teamMemberDTOS = team.getUsers().stream().map(user -> toTeamMemberDTO(user, owner)).toList();
        boolean currentUserOwner = currentUser.getId() == owner.getId();

        return new TeamDetailDTO(team.getId(), team.getName(), ownerName, memberCount, totalScore, teamMemberDTOS, team.getInviteCode(), currentUserOwner);
    }

    private TeamMemberDTO toTeamMemberDTO(User user, User teamOwner){

        String displayName = user.getFirstName() + " " + user.getLastName();
        int score = predictionService.calculateTotalScoreForUser(user);
        boolean isOwner = user.getId() == teamOwner.getId();

        return new TeamMemberDTO(user.getId(), displayName, score, isOwner);
    }

    private void removeMember(Team team, User member){
        if (member.getTeam() == null)
            throw new IllegalArgumentException("This user doesn't have a team");

        if (member.getTeam().getId() != team.getId())
            throw new IllegalArgumentException("You can't remove users that aren't in your team");

        if (member.getId() == team.getOwner().getId())
            throw new IllegalArgumentException("The team owner cannot be removed");

        member.removeTeam();
        userRepository.save(member);
    }

}
