package com.example.wk_prognose.controller;

import com.example.wk_prognose.dto.request.CreateTeamDTO;
import com.example.wk_prognose.dto.request.JoinTeamDTO;
import com.example.wk_prognose.dto.response.TeamDetailDTO;
import com.example.wk_prognose.exception.TeamNotFoundException;
import com.example.wk_prognose.service.RankingService;
import com.example.wk_prognose.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;
    private final RankingService rankingService;

    @GetMapping
    public String getMyTeam(Model model) {
        if (teamService.currentUserHasTeam()) {
            return "redirect:" + teamService.findMyTeamLink();
        }
        return "team-empty";
    }

    @GetMapping("{id}")
    public String getTeam(@PathVariable Long id, Model model){
        TeamDetailDTO teamDetailDTO = teamService.findTeamById(id).orElseThrow(() ->
                new TeamNotFoundException("No team found with this id"));

        model.addAttribute("teamDetailDTO", teamDetailDTO);
        model.addAttribute("rankedMembers", rankingService.findRankedMembersForTeam(id));
        model.addAttribute("teamTotalScore", rankingService.calculateTotalScoreForTeam(id));

        return "team-detail";
    }

    @GetMapping("ranking")
    public String getTop10Teams(Model model) {
        model.addAttribute("topTeams", rankingService.findTop10Teams());

        return "top-teams";
    }


    @GetMapping("create")
    public String showCreateTeamForm(CreateTeamDTO createTeamDTO, Model model){

        return "create-team";
    }

    @PostMapping("create")
    public String createTeam(@Valid CreateTeamDTO createTeamDTO, BindingResult result, Model model){

        if (result.hasErrors()) {
            return "create-team";
        }

        //TODO validatie (teamnaam te kort ofzo of bestaat al)
        // TODO exception handling (teamnaam bestaat al, ge hebt al een team,..)
        try{
            teamService.createTeam(createTeamDTO);
        } catch (IllegalArgumentException ex){
            return "create-team";
        }


        return "redirect:" + teamService.findMyTeamLink();
    }

    @GetMapping("join")
    public String showJoinTeamForm(JoinTeamDTO joinTeamDTO, Model model){

        return "join-team";
    }

    @PostMapping("join")
    public String joinTeam(@Valid JoinTeamDTO joinTeamDTO, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "join-team";
        }

        try{
            // TODO validatie (lang genoeg?)
            teamService.joinTeam(joinTeamDTO);
        } catch (IllegalArgumentException ex){
            // TODO exception handling (code bestaat niet?)
            return "join-team";
        }

        return "redirect:" + teamService.findMyTeamLink();
    }

    @PostMapping("{id}/regenerate-invite-code")
    public String regenerateInviteCode(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try {
            teamService.regenerateInviteCode(id);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        return "redirect:/teams/" + id;
    }

    @PostMapping("{teamId}/members/{memberId}/remove")
    public String removeMemberFromTeam(@PathVariable Long teamId, @PathVariable Long memberId){
        teamService.removeMember(teamId,memberId);

        return "redirect:/teams/" + teamId;
    }
}
