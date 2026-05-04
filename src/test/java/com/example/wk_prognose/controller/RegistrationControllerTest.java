package com.example.wk_prognose.controller;

import com.example.wk_prognose.dto.request.InputRegistrationDTO;
import com.example.wk_prognose.service.CurrentUserService;
import com.example.wk_prognose.service.TeamService;
import com.example.wk_prognose.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(RegistrationController.class)
@AutoConfigureMockMvc(addFilters = false)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService mockService;

    @MockitoBean
    private CurrentUserService currentUserService;

    @MockitoBean
    private TeamService teamService;

    @Test
    void testGetRequest() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("inputRegistrationDTO"));
    }

    @Test
    void testPostRequestValidRegistration() throws Exception {
        InputRegistrationDTO validRegistration = new InputRegistrationDTO(
                "Nicolas", "Test", "nicolas@test.be", "secret");

        mockMvc.perform(post("/register").flashAttr("inputRegistrationDTO", validRegistration))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(mockService).registerUser(any(InputRegistrationDTO.class));
    }

    private static Stream<Arguments> invalidRegistrationData() {
        return Stream.of(
                Arguments.of(new InputRegistrationDTO("", "Test", "nicolas@test.be", "secret"), new String[]{"firstName"}),
                Arguments.of(new InputRegistrationDTO("Nicolas", "", "nicolas@test.be", "secret"), new String[]{"lastName"}),
                Arguments.of(new InputRegistrationDTO("Nicolas", "Test", "wrong", "secret"), new String[]{"email"}),
                Arguments.of(new InputRegistrationDTO("Nicolas", "Test", "nicolas@test.be", ""), new String[]{"password"}),
                Arguments.of(new InputRegistrationDTO("", "", "wrong", ""), new String[]{"firstName", "lastName", "email", "password"})
        );
    }

    @ParameterizedTest
    @MethodSource("invalidRegistrationData")
    void testPostRequestInvalidRegistration(InputRegistrationDTO invalidRegistration, String[] expectedErrors) throws Exception {
        mockMvc.perform(post("/register").flashAttr("inputRegistrationDTO", invalidRegistration))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("inputRegistrationDTO", expectedErrors));

        verify(mockService, never()).registerUser(any());
    }
}
