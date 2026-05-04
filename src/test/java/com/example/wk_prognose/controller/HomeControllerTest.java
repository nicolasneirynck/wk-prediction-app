package com.example.wk_prognose.controller;

import com.example.wk_prognose.dto.response.MatchDTO;
import com.example.wk_prognose.service.CurrentUserService;
import com.example.wk_prognose.service.MatchService;
import com.example.wk_prognose.service.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatchService mockService;

    @MockitoBean
    private CurrentUserService currentUserService;

    @MockitoBean
    private TeamService teamService;

    @Test
    void testGetRequest() throws Exception {
        List<MatchDTO> expectedMatches = List.of();
        when(mockService.findAllMatches()).thenReturn(expectedMatches);

        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("matches"))
                .andExpect(model().attribute("matches", expectedMatches));

        verify(mockService).findAllMatches();
    }
}
