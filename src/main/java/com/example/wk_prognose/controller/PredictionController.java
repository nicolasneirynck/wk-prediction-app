package com.example.wk_prognose.controller;

import com.example.wk_prognose.dto.request.InputPredictionDTO;
import com.example.wk_prognose.dto.response.PredictionDTO;
import com.example.wk_prognose.service.MatchService;
import com.example.wk_prognose.service.PredictionService;
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
@RequestMapping("predictions")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionService predictionService;
    private final TeamService teamService;
    private final MatchService matchService;

    @GetMapping()
    public String showPredictions(Model model){
        model.addAttribute("upcomingPredictions", predictionService.findUpcomingPredictionsForCurrentUser());
        model.addAttribute("pastPredictions", predictionService.findPastPredictionsForCurrentUser());
        model.addAttribute("totalPoints", predictionService.calculateTotalScoreForCurrentUser());
        model.addAttribute("myTeamLink", teamService.findMyTeamLink());

        return "predictions";
    }

    @GetMapping("/add/{matchId}")
    public String showAddPrediction(@PathVariable Long matchId, InputPredictionDTO inputPredictionDTO, Model model){
        preparePredictionFormViewData(model, inputPredictionDTO, "predictions/add/" + matchId, "Add Prediction");
        return "add-prediction";
    }

    @PostMapping("/add/{matchId}")
    public String addPrediction(@PathVariable Long matchId, @Valid InputPredictionDTO inputPredictionDTO,
                                 BindingResult result, Model model, RedirectAttributes redirectAttributes){

        if (result.hasErrors()) {
            preparePredictionFormViewData(model, inputPredictionDTO, "predictions/add/" + matchId, "Add Prediction");

            return "add-prediction";
        }

        try{
            predictionService.savePrediction(matchId, inputPredictionDTO);
        } catch (IllegalArgumentException ex){
            preparePredictionFormViewData(model, inputPredictionDTO, "predictions/add/" + matchId, "Add Prediction");
            model.addAttribute("errorMessage", ex.getMessage());

            return "add-prediction";
        }

        redirectAttributes.addFlashAttribute("successMessageKey", "prediction.add.success");
        return "redirect:/predictions/matches";

    }

    @GetMapping("/edit/{matchId}")
    public String showEditPrediction(@PathVariable Long matchId, Model model){

        PredictionDTO predictionDTO = predictionService.findPredictionByMatchId(matchId).orElseThrow(() ->
                new IllegalArgumentException("No match found with this id"));

        InputPredictionDTO existingInputPredictionDTO = new InputPredictionDTO(predictionDTO.predictedHomeScore(), predictionDTO.predictedAwayScore());

        preparePredictionFormViewData(model, existingInputPredictionDTO, "predictions/edit/" + matchId, "Edit Prediction");

        return "edit-prediction";
    }

    @PostMapping("/edit/{matchId}")
    public String editPrediction(@PathVariable Long matchId, @Valid InputPredictionDTO inputPredictionDTO,
                                BindingResult result, Model model, RedirectAttributes redirectAttributes){

        if (result.hasErrors()) {
            preparePredictionFormViewData(model, inputPredictionDTO, "predictions/edit/" + matchId, "Edit Prediction");

            return "edit-prediction";
        }

        try{
            predictionService.savePrediction(matchId, inputPredictionDTO);
        } catch (IllegalArgumentException ex){
            preparePredictionFormViewData(model, inputPredictionDTO, "predictions/edit/" + matchId, "Edit Prediction");
            model.addAttribute("errorMessage", ex.getMessage());

            return "edit-prediction";
        }

        redirectAttributes.addFlashAttribute("successMessageKey", "prediction.edit.success");
        return "redirect:/predictions/matches";

    }

    private void preparePredictionFormViewData(Model model, InputPredictionDTO inputPredictionDTO, String formAction,
                                               String submitLabel){

        model.addAttribute("inputPredictionDTO", inputPredictionDTO);
        model.addAttribute("myTeamLink", teamService.findMyTeamLink());
        model.addAttribute("formAction", formAction);
        model.addAttribute("submitLabel", submitLabel);
    }
}
