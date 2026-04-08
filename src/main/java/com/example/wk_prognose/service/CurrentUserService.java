package com.example.wk_prognose.service;

import com.example.wk_prognose.model.User;
import com.example.wk_prognose.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public Optional<User> getCurrentUser(){
        return userRepository.findByEmail("test@mail.com");
    }
}
