package com.example.wk_prognose.service;

import com.example.wk_prognose.dto.request.InputRegistrationDTO;
import com.example.wk_prognose.model.User;
import com.example.wk_prognose.repository.UserRepository;
import com.example.wk_prognose.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: ".formatted(email)));
    }

    public void registerUser(InputRegistrationDTO inputRegistrationDTO){
        User newUser = new User(
                inputRegistrationDTO.firstName(),
                inputRegistrationDTO.lastName(),
                inputRegistrationDTO.email(),
                passwordEncoder.encode(inputRegistrationDTO.password()),
                Role.USER);

        userRepository.save(newUser);
    }

}

