package com.example.wk_prognose.controller;

import com.example.wk_prognose.dto.request.CreateTeamDTO;
import com.example.wk_prognose.dto.request.JoinTeamDTO;
import com.example.wk_prognose.dto.response.TeamDetailDTO;
import com.example.wk_prognose.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public String getMyTeam(Model model) {
        if (teamService.currentUserHasTeam()) {
            return "redirect:" + teamService.findMyTeamLink();
        }

        model.addAttribute("myTeamLink", "/teams"); // sidenav heeft deze link nodig

        return "team-empty";
    }

    @GetMapping("{id}")
    public String getTeam(@PathVariable Long id, Model model){
        TeamDetailDTO teamDetailDTO = teamService.findTeamById(id).orElseThrow(() ->
                new IllegalArgumentException("No team found with this id"));

        model.addAttribute("teamDetailDTO", teamDetailDTO);
        model.addAttribute("myTeamLink", teamService.findMyTeamLink());

        return "team-detail";
    }


    @GetMapping("create")
    public String showCreateTeamForm(CreateTeamDTO createTeamDTO, Model model){
        model.addAttribute("myTeamLink", teamService.findMyTeamLink());

        return "create-team";
    }

    @PostMapping("create")
    public String createTeam(CreateTeamDTO createTeamDTO, Model model){

        //TODO validatie (teamnaam te kort ofzo of bestaat al)
        // TODO exception handling (teamnaam bestaat al, ge hebt al een team,..)
        try{
            teamService.createTeam(createTeamDTO);
        } catch (IllegalArgumentException ex){
            model.addAttribute("myTeamLink", teamService.findMyTeamLink());
            return "create-team";
        }


        return "redirect:" + teamService.findMyTeamLink();
    }

    @GetMapping("join")
    public String showJoinTeamForm(JoinTeamDTO joinTeamDTO, Model model){

        model.addAttribute("myTeamLink", teamService.findMyTeamLink());

        return "join-team";
    }

    @PostMapping("join")
    public String joinTeam(JoinTeamDTO joinTeamDTO, Model model){
        try{
            // TODO validatie (lang genoeg?)
            teamService.joinTeam(joinTeamDTO);
        } catch (IllegalArgumentException ex){
            model.addAttribute("myTeamLink", teamService.findMyTeamLink());
            // TODO exception handling (code bestaat niet?)
            return "join-team";
        }

        return "redirect:" + teamService.findMyTeamLink();
    }

    @PostMapping("{id}/regenerate-invite-code")
    public String regenerateInviteCode(@PathVariable Long id){
        teamService.regenerateInviteCode(id);

        return "redirect:/teams/" + id;
    }

    @PostMapping("{teamId}/members/{memberId}/remove")
    public String removeMemberFromTeam(@PathVariable Long teamId, @PathVariable Long memberId){
        teamService.removeMember(teamId,memberId);

        return "redirect:/teams/" + teamId;
    }
}
