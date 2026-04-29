package com.example.wk_prognose.advice;

import com.example.wk_prognose.model.User;
import com.example.wk_prognose.service.CurrentUserService;
import com.example.wk_prognose.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final CurrentUserService currentUserService;
    private final TeamService teamService;

    @ModelAttribute("currentUserDisplayName")
    public String populateUsername() {
        return currentUserService.getCurrentUser()
                .map(User::getDisplayName)
                .orElse("");
    }

    @ModelAttribute("myTeamLink")
    public String populateMyTeamLink() {
        return currentUserService.getCurrentUser().isPresent()
                ? teamService.findMyTeamLink()
                : "/teams";
    }
}
