package com.example.wk_prognose.controller;

import com.example.wk_prognose.dto.request.InputMatchDTO;
import com.example.wk_prognose.dto.response.MatchDTO;
import com.example.wk_prognose.service.MatchService;
import com.example.wk_prognose.service.TeamService;
import com.example.wk_prognose.util.MatchStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;
    private final TeamService teamService;

    @GetMapping("/{id}")
    public String getMatch(@PathVariable Long id, Model model){
        MatchDTO matchDTO = matchService.findMatchById(id).orElseThrow(() ->
                new IllegalArgumentException("No match found with this id"));

        model.addAttribute("matchDTO", matchDTO);
        model.addAttribute("myTeamLink", teamService.findMyTeamLink());

        return "match-detail";
    }

    @GetMapping("/manage")
    public String showManageMatches(InputMatchDTO inputMatchDTO, Model model){
        model.addAttribute("matches", matchService.findAllMatches());
        model.addAttribute("myTeamLink", teamService.findMyTeamLink());
        model.addAttribute("inputMatchDTO", inputMatchDTO);

        return "manage-matches";
    }

    @GetMapping("/add")
    public String showAddMatch(InputMatchDTO inputMatchDTO, Model model){
        prepareMatchFormViewData(model, inputMatchDTO, "/matches/add", "Create Match", false);
        return "add-match";
    }

    @PostMapping("/add")
    public String addMatch(@Valid InputMatchDTO inputMatchDTO, BindingResult result, Model model,
                           RedirectAttributes redirectAttributes){

        if (result.hasErrors()) {
            prepareMatchFormViewData(model, inputMatchDTO, "/matches/add", "Create Match", false);

            return "add-match";
        }

        try {
            matchService.addMatch(inputMatchDTO);
        } catch (IllegalArgumentException ex) {
            prepareMatchFormViewData(model, inputMatchDTO, "/matches/add", "Create Match", false);
            model.addAttribute("errorMessage", ex.getMessage());

            return "add-match";
        }

        redirectAttributes.addFlashAttribute("successMessageKey", "match.add.success");
        return "redirect:/matches/manage";
    }

    @GetMapping("/edit/{id}")
    public String showEditMatch(@PathVariable Long id, Model model){

        MatchDTO matchDTO = matchService.findMatchById(id).orElseThrow(() ->
                new IllegalArgumentException("No match found with this id"));

        InputMatchDTO existingInputMatchDTO = new InputMatchDTO(
                matchDTO.id(),
                matchDTO.homeCountryEnum(),
                matchDTO.awayCountryEnum(),
                matchDTO.matchDateTime(),
                matchDTO.locationId(),
                matchDTO.stadiumCode(),
                matchDTO.checksum(),
                matchDTO.matchStage(),
                matchDTO.finalHomeScore(),
                matchDTO.finalAwayScore()
        );

        boolean showScoreInputs = matchDTO.matchStatus() == MatchStatus.AWAITING_RESULT
                || matchDTO.matchStatus() == MatchStatus.COMPLETED;

        prepareMatchFormViewData(model, existingInputMatchDTO, "/matches/edit/" + id, "Save Match", showScoreInputs);
        model.addAttribute("matchDTO", matchDTO);

        return "edit-match";
    }

    @PostMapping("/edit/{id}")
    public String editMatch(@PathVariable Long id, @Valid InputMatchDTO inputMatchDTO, BindingResult result,
                            Model model, RedirectAttributes redirectAttributes){
        boolean showScoreInputs = matchService.findMatchById(id)
                .map(match -> match.matchStatus() == MatchStatus.AWAITING_RESULT
                        || match.matchStatus() == MatchStatus.COMPLETED)
                .orElse(false);

        if (result.hasErrors()) {
            prepareMatchFormViewData(model, inputMatchDTO, "/matches/edit/" + id, "Save Match", showScoreInputs);

            return "edit-match";
        }

        try {
            matchService.editMatch(id, inputMatchDTO);
        } catch (IllegalArgumentException ex) {
            prepareMatchFormViewData(model, inputMatchDTO, "/matches/edit/" + id, "Save Match", showScoreInputs);
            model.addAttribute("errorMessage", ex.getMessage());

            return "edit-match";
        }

        redirectAttributes.addFlashAttribute("successMessageKey", "match.edit.success");
        return "redirect:/matches/manage";
    }

    private void prepareMatchFormViewData(Model model, InputMatchDTO inputMatchDTO, String formAction,
                                          String submitLabel, boolean showScoreInputs) {
        model.addAttribute("inputMatchDTO", inputMatchDTO);
        model.addAttribute("locations", matchService.findAllLocations());
        model.addAttribute("myTeamLink", teamService.findMyTeamLink());
        model.addAttribute("formAction", formAction);
        model.addAttribute("submitLabel", submitLabel);
        model.addAttribute("showScoreInputs", showScoreInputs);
    }
}
