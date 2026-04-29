package com.example.wk_prognose.controller;

import com.example.wk_prognose.dto.request.InputRegistrationDTO;
import com.example.wk_prognose.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("register")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping
    public String getRegisterForm(InputRegistrationDTO inputRegistrationDTO){
        return "register";
    }

    @PostMapping
    public String register(@Valid InputRegistrationDTO inputRegistrationDTO, BindingResult result){
        if (result.hasErrors())
            return "register";

        userService.registerUser(inputRegistrationDTO);

        return "redirect:/login";
    }
}
