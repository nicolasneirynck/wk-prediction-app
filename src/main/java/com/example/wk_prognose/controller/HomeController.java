package com.example.wk_prognose.controller;

import com.example.wk_prognose.service.MatchService;
import com.example.wk_prognose.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MatchService matchService;

    @GetMapping("/home")
    public String showHomePage(Model model){
        model.addAttribute("matches", matchService.findAllMatches());
        return "home";
    }

}
