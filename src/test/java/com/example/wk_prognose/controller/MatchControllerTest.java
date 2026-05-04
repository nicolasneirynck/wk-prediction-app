package com.example.wk_prognose.controller;

import com.example.wk_prognose.dto.request.InputMatchDTO;
import com.example.wk_prognose.dto.request.InputPredictionDTO;
import com.example.wk_prognose.dto.response.MatchDTO;
import com.example.wk_prognose.dto.response.PredictionDTO;
import com.example.wk_prognose.repository.MatchRepository;
import com.example.wk_prognose.service.CurrentUserService;
import com.example.wk_prognose.service.MatchService;
import com.example.wk_prognose.service.PredictionService;
import com.example.wk_prognose.service.TeamService;
import com.example.wk_prognose.util.Country;
import com.example.wk_prognose.util.MatchStage;
import com.example.wk_prognose.util.MatchStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(MatchController.class)
@AutoConfigureMockMvc(addFilters = false)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MatchService mockService;

    @MockitoBean
    private PredictionService predictionService;

    @MockitoBean
    private CurrentUserService currentUserService;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private MatchRepository matchRepository;

    @Test
    void testGetMatch_NoPredictionFound() throws Exception {
        MatchDTO expectedMatch = matchDTO(MatchStatus.UPCOMING);
        when(mockService.findMatchById(1L)).thenReturn(Optional.of(expectedMatch));
        when(predictionService.findPredictionByMatchId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/matches/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("match-detail"))
                .andExpect(model().attribute("matchDTO", expectedMatch))
                .andExpect(model().attributeExists("inputPredictionDTO"))
                .andExpect(model().attribute("predictionFormAction", "/matches/1/prediction"))
                .andExpect(model().attribute("predictionSubmitLabel", "Save prediction"));

        verify(mockService).findMatchById(1L);
        verify(predictionService, times(2)).findPredictionByMatchId(1L);
    }

    @Test
    void testGetMatch_PredictionFound() throws Exception {
        MatchDTO expectedMatch = matchDTO(MatchStatus.UPCOMING);
        PredictionDTO predictionDTO = new PredictionDTO(1L, 2, 1);
        when(mockService.findMatchById(1L)).thenReturn(Optional.of(expectedMatch));
        when(predictionService.findPredictionByMatchId(1L)).thenReturn(Optional.of(predictionDTO));

        mockMvc.perform(get("/matches/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("match-detail"))
                .andExpect(model().attribute("matchDTO", expectedMatch))
                .andExpect(model().attribute("inputPredictionDTO", new InputPredictionDTO(2, 1)))
                .andExpect(model().attribute("predictionSubmitLabel", "Update prediction"));

        verify(mockService).findMatchById(1L);
        verify(predictionService, times(2)).findPredictionByMatchId(1L);
    }

    @Test
    void testPostRequestValidPrediction() throws Exception {
        MatchDTO expectedMatch = matchDTO(MatchStatus.UPCOMING);
        InputPredictionDTO predictionDTO = new InputPredictionDTO(2, 1);
        when(mockService.findMatchById(1L)).thenReturn(Optional.of(expectedMatch));
        when(predictionService.findPredictionByMatchId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/matches/1/prediction").flashAttr("inputPredictionDTO", predictionDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/predictions"));

        verify(predictionService).savePrediction(1L, predictionDTO);
    }

    @ParameterizedTest
    @MethodSource("invalidPredictionData")
    void testPostRequestInvalidPrediction(InputPredictionDTO predictionDTO, String[] expectedErrors) throws Exception {
        MatchDTO expectedMatch = matchDTO(MatchStatus.UPCOMING);
        when(mockService.findMatchById(1L)).thenReturn(Optional.of(expectedMatch));
        when(predictionService.findPredictionByMatchId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/matches/1/prediction").flashAttr("inputPredictionDTO", predictionDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("match-detail"))
                .andExpect(model().attributeHasFieldErrors("inputPredictionDTO", expectedErrors));

        verify(predictionService, never()).savePrediction(any(), any());
    }

    @Test
    void testPostRequestPrediction_ServiceException() throws Exception {
        MatchDTO expectedMatch = matchDTO(MatchStatus.UPCOMING);
        InputPredictionDTO predictionDTO = new InputPredictionDTO(2, 1);
        when(mockService.findMatchById(1L)).thenReturn(Optional.of(expectedMatch));
        when(predictionService.findPredictionByMatchId(1L)).thenReturn(Optional.empty());
        doThrow(new IllegalArgumentException("Deadline passed")).when(predictionService).savePrediction(1L, predictionDTO);

        mockMvc.perform(post("/matches/1/prediction").flashAttr("inputPredictionDTO", predictionDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("match-detail"))
                .andExpect(model().attributeExists("errorMessage"));

        verify(predictionService).savePrediction(1L, predictionDTO);
    }

    @Test
    void testGetManageMatches() throws Exception {
        List<MatchDTO> expectedMatches = List.of(matchDTO(MatchStatus.UPCOMING));
        when(mockService.findAllMatches()).thenReturn(expectedMatches);

        mockMvc.perform(get("/matches/manage"))
                .andExpect(status().isOk())
                .andExpect(view().name("manage-matches"))
                .andExpect(model().attribute("matches", expectedMatches))
                .andExpect(model().attributeExists("inputMatchDTO"));

        verify(mockService).findAllMatches();
    }

    @Test
    void testGetAddMatch() throws Exception {
        when(mockService.findAllLocations()).thenReturn(List.of());

        mockMvc.perform(get("/matches/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-match"))
                .andExpect(model().attributeExists("inputMatchDTO", "locations"))
                .andExpect(model().attribute("formAction", "/matches/add"))
                .andExpect(model().attribute("submitLabel", "Create Match"))
                .andExpect(model().attribute("showScoreInputs", false));

        verify(mockService).findAllLocations();
    }

    @Test
    void testPostRequestValidAddMatch() throws Exception {
        InputMatchDTO inputMatchDTO = validMatchDTO(null);
        when(matchRepository.existsByLocationIdAndMatchDateTime(1L, inputMatchDTO.matchDateTime())).thenReturn(false);

        mockMvc.perform(post("/matches/add").flashAttr("inputMatchDTO", inputMatchDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/matches/manage"));

        verify(mockService).addMatch(inputMatchDTO);
    }

    @ParameterizedTest
    @MethodSource("invalidMatchData")
    void testPostRequestInvalidAddMatch(InputMatchDTO inputMatchDTO, String[] expectedErrors) throws Exception {
        mockMvc.perform(post("/matches/add").flashAttr("inputMatchDTO", inputMatchDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("add-match"))
                .andExpect(model().attributeHasFieldErrors("inputMatchDTO", expectedErrors));

        verify(mockService, never()).addMatch(any());
    }

    @Test
    void testPostRequestAddMatch_ServiceException() throws Exception {
        InputMatchDTO inputMatchDTO = validMatchDTO(null);
        when(matchRepository.existsByLocationIdAndMatchDateTime(1L, inputMatchDTO.matchDateTime())).thenReturn(false);
        doThrow(new IllegalArgumentException("No location found")).when(mockService).addMatch(inputMatchDTO);

        mockMvc.perform(post("/matches/add").flashAttr("inputMatchDTO", inputMatchDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("add-match"))
                .andExpect(model().attributeExists("errorMessage"));

        verify(mockService).addMatch(inputMatchDTO);
    }

    @Test
    void testGetEditMatch() throws Exception {
        MatchDTO expectedMatch = matchDTO(MatchStatus.AWAITING_RESULT);
        when(mockService.findMatchById(1L)).thenReturn(Optional.of(expectedMatch));
        when(mockService.findAllLocations()).thenReturn(List.of());

        mockMvc.perform(get("/matches/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-match"))
                .andExpect(model().attributeExists("inputMatchDTO", "locations"))
                .andExpect(model().attribute("matchDTO", expectedMatch))
                .andExpect(model().attribute("formAction", "/matches/edit/1"))
                .andExpect(model().attribute("submitLabel", "Save Match"))
                .andExpect(model().attribute("showScoreInputs", true));

        verify(mockService).findMatchById(1L);
        verify(mockService).findAllLocations();
    }

    @Test
    void testPostRequestValidEditMatch() throws Exception {
        InputMatchDTO inputMatchDTO = validMatchDTO(1L);
        when(mockService.findMatchById(1L)).thenReturn(Optional.of(matchDTO(MatchStatus.UPCOMING)));
        when(matchRepository.existsByLocationIdAndMatchDateTimeAndIdNot(1L, inputMatchDTO.matchDateTime(), 1L)).thenReturn(false);

        mockMvc.perform(post("/matches/edit/1").flashAttr("inputMatchDTO", inputMatchDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/matches/manage"));

        verify(mockService).editMatch(1L, inputMatchDTO);
    }

    @ParameterizedTest
    @MethodSource("invalidMatchData")
    void testPostRequestInvalidEditMatch(InputMatchDTO inputMatchDTO, String[] expectedErrors) throws Exception {
        when(mockService.findMatchById(1L)).thenReturn(Optional.of(matchDTO(MatchStatus.UPCOMING)));

        mockMvc.perform(post("/matches/edit/1").flashAttr("inputMatchDTO", inputMatchDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-match"))
                .andExpect(model().attributeHasFieldErrors("inputMatchDTO", expectedErrors));

        verify(mockService, never()).editMatch(any(), any());
    }

    private static Stream<Arguments> invalidPredictionData() {
        return Stream.of(
                Arguments.of(new InputPredictionDTO(null, 1), new String[]{"predictedHomeScore"}),
                Arguments.of(new InputPredictionDTO(1, null), new String[]{"predictedAwayScore"}),
                Arguments.of(new InputPredictionDTO(-1, 1), new String[]{"predictedHomeScore"}),
                Arguments.of(new InputPredictionDTO(1, -1), new String[]{"predictedAwayScore"})
        );
    }

    private static Stream<Arguments> invalidMatchData() {
        return Stream.of(
                Arguments.of(new InputMatchDTO(null, null, Country.FRANCE, validDate(), 1L, 97, 0, MatchStage.GROUP_A, null, null), new String[]{"homeCountry"}),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, null, validDate(), 1L, 97, 0, MatchStage.GROUP_A, null, null), new String[]{"awayCountry"}),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, null, 1L, 97, 0, MatchStage.GROUP_A, null, null), new String[]{"matchDateTime"}),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, validDate(), null, 97, 0, MatchStage.GROUP_A, null, null), new String[]{"locationId"}),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.FRANCE, validDate(), 1L, 97, 1, MatchStage.GROUP_A, null, null), new String[]{"checksum"}),
                Arguments.of(new InputMatchDTO(null, Country.BELGIUM, Country.BELGIUM, validDate(), 1L, 97, 0, MatchStage.GROUP_A, null, null), new String[]{"awayCountry"})
        );
    }

    private static InputMatchDTO validMatchDTO(Long id) {
        return new InputMatchDTO(
                id,
                Country.BELGIUM,
                Country.FRANCE,
                validDate(),
                1L,
                97,
                0,
                MatchStage.GROUP_A,
                null,
                null
        );
    }

    private static MatchDTO matchDTO(MatchStatus status) {
        return new MatchDTO(
                1L,
                "Belgium",
                "be",
                Country.BELGIUM,
                "France",
                "fr",
                Country.FRANCE,
                validDate(),
                1L,
                "Brussels",
                "BELGIUM",
                "Test Stadium",
                MatchStage.GROUP_A,
                97,
                0,
                null,
                null,
                status
        );
    }

    private static LocalDateTime validDate() {
        return LocalDateTime.of(2026, 6, 12, 20, 0);
    }
}
