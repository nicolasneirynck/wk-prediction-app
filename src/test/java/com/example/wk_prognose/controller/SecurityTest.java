package com.example.wk_prognose.controller;

import com.example.wk_prognose.config.SecurityConfig;
import com.example.wk_prognose.dto.response.MatchDTO;
import com.example.wk_prognose.repository.MatchRepository;
import com.example.wk_prognose.service.CurrentUserService;
import com.example.wk_prognose.service.MatchService;
import com.example.wk_prognose.service.PredictionService;
import com.example.wk_prognose.service.RankingService;
import com.example.wk_prognose.service.TeamService;
import com.example.wk_prognose.service.UserService;
import com.example.wk_prognose.util.Country;
import com.example.wk_prognose.util.MatchStage;
import com.example.wk_prognose.util.MatchStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest
@Import(SecurityConfig.class)
class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatchService matchService;

    @MockitoBean
    private PredictionService predictionService;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private RankingService rankingService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CurrentUserService currentUserService;

    @MockitoBean
    private MatchRepository matchRepository;

    @ParameterizedTest
    @CsvSource({"/login, login", "/home, home", "/teams/ranking, top-teams"})
    void testPublicPages(String url, String expectedViewName) throws Exception {
        when(matchService.findAllMatches()).thenReturn(List.of());
        when(rankingService.findTop10Teams()).thenReturn(List.of());

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(view().name(expectedViewName));
    }

    @WithAnonymousUser
    @ParameterizedTest
    @CsvSource({"/teams", "/predictions", "/matches/manage", "/matches/add", "/matches/edit/1"})
    void testNoAccessAnonymous(String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testAccessTeamsWithUserRole() throws Exception {
        when(teamService.currentUserHasTeam()).thenReturn(false);

        mockMvc.perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(view().name("team-empty"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testAccessPredictionsWithUserRole() throws Exception {
        when(predictionService.findUpcomingPredictionsForCurrentUser()).thenReturn(List.of());
        when(predictionService.findPastPredictionsForCurrentUser()).thenReturn(List.of());
        when(predictionService.calculateTotalScoreForCurrentUser()).thenReturn(0);

        mockMvc.perform(get("/predictions"))
                .andExpect(status().isOk())
                .andExpect(view().name("predictions"))
                .andExpect(model().attributeExists("upcomingPredictions", "pastPredictions", "totalPoints"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testPostPredictionWithUserRole() throws Exception {
        when(matchService.findMatchById(1L)).thenReturn(Optional.of(matchDTO()));
        when(predictionService.findPredictionByMatchId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/matches/1/prediction")
                        .with(csrf())
                        .param("predictedHomeScore", "2")
                        .param("predictedAwayScore", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/predictions"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @ParameterizedTest
    @CsvSource({"/matches/manage", "/matches/add", "/matches/edit/1"})
    void testNoAccessAdminPagesWithUserRole(String url) throws Exception {
        mockMvc.perform(get(url))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAccessManageMatchesWithAdminRole() throws Exception {
        when(matchService.findAllMatches()).thenReturn(List.of());

        mockMvc.perform(get("/matches/manage"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-matches"))
                .andExpect(model().attributeExists("matches", "inputMatchDTO"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAccessAddMatchWithAdminRole() throws Exception {
        when(matchService.findAllLocations()).thenReturn(List.of());

        mockMvc.perform(get("/matches/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-match"))
                .andExpect(model().attributeExists("inputMatchDTO", "locations"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAccessEditMatchWithAdminRole() throws Exception {
        when(matchService.findMatchById(1L)).thenReturn(Optional.of(matchDTO()));
        when(matchService.findAllLocations()).thenReturn(List.of());

        mockMvc.perform(get("/matches/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-match"))
                .andExpect(model().attributeExists("inputMatchDTO", "locations", "matchDTO"));
    }

    private MatchDTO matchDTO() {
        return new MatchDTO(
                1L,
                "Belgium",
                "be",
                Country.BELGIUM,
                "France",
                "fr",
                Country.FRANCE,
                LocalDateTime.of(2026, 6, 12, 20, 0),
                1L,
                "Brussels",
                "BELGIUM",
                "Test Stadium",
                MatchStage.GROUP_A,
                97,
                0,
                null,
                null,
                MatchStatus.UPCOMING
        );
    }
}
