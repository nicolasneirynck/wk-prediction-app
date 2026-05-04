package com.example.wk_prognose.controller;

import com.example.wk_prognose.dto.request.InputPredictionDTO;
import com.example.wk_prognose.dto.response.PredictionDTO;
import com.example.wk_prognose.dto.response.PredictionOverviewDTO;
import com.example.wk_prognose.service.CurrentUserService;
import com.example.wk_prognose.service.PredictionService;
import com.example.wk_prognose.service.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.AbstractView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PredictionController.class)
@AutoConfigureMockMvc(addFilters = false)
class PredictionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PredictionService mockService;

    @MockitoBean
    private CurrentUserService currentUserService;

    @MockitoBean
    private TeamService teamService;

    @TestConfiguration
    static class TestViewConfig {

        @Bean
        ViewResolver testViewResolver() {
            return new TestViewResolver();
        }

        private static class TestViewResolver implements ViewResolver, Ordered {

            @Override
            public View resolveViewName(String viewName, Locale locale) {
                if (viewName.startsWith("redirect:")) {
                    return null;
                }

                return new AbstractView() {
                    @Override
                    protected void renderMergedOutputModel(Map<String, Object> model,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response) {
                    }
                };
            }

            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }
        }
    }

    @Test
    void testGetRequest() throws Exception {
        List<PredictionOverviewDTO> upcomingPredictions = List.of();
        List<PredictionOverviewDTO> pastPredictions = List.of();
        when(mockService.findUpcomingPredictionsForCurrentUser()).thenReturn(upcomingPredictions);
        when(mockService.findPastPredictionsForCurrentUser()).thenReturn(pastPredictions);
        when(mockService.calculateTotalScoreForCurrentUser()).thenReturn(12);

        mockMvc.perform(get("/predictions"))
                .andExpect(status().isOk())
                .andExpect(view().name("predictions"))
                .andExpect(model().attribute("upcomingPredictions", upcomingPredictions))
                .andExpect(model().attribute("pastPredictions", pastPredictions))
                .andExpect(model().attribute("totalPoints", 12));

        verify(mockService).findUpcomingPredictionsForCurrentUser();
        verify(mockService).findPastPredictionsForCurrentUser();
        verify(mockService).calculateTotalScoreForCurrentUser();
    }

    @Test
    void testGetAddPrediction() throws Exception {
        mockMvc.perform(get("/predictions/add/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-prediction"))
                .andExpect(model().attributeExists("inputPredictionDTO"))
                .andExpect(model().attribute("formAction", "predictions/add/1"))
                .andExpect(model().attribute("submitLabel", "Add Prediction"));
    }

    @Test
    void testPostRequestValidAddPrediction() throws Exception {
        InputPredictionDTO predictionDTO = new InputPredictionDTO(2, 1);

        mockMvc.perform(post("/predictions/add/1").flashAttr("inputPredictionDTO", predictionDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/predictions/matches"));

        verify(mockService).savePrediction(1L, predictionDTO);
    }

    private static Stream<Arguments> invalidPredictionData() {
        return Stream.of(
                Arguments.of(new InputPredictionDTO(null, 1), new String[]{"predictedHomeScore"}),
                Arguments.of(new InputPredictionDTO(1, null), new String[]{"predictedAwayScore"}),
                Arguments.of(new InputPredictionDTO(-1, 1), new String[]{"predictedHomeScore"}),
                Arguments.of(new InputPredictionDTO(1, -1), new String[]{"predictedAwayScore"})
        );
    }

    @ParameterizedTest
    @MethodSource("invalidPredictionData")
    void testPostRequestInvalidAddPrediction(InputPredictionDTO predictionDTO, String[] expectedErrors) throws Exception {
        mockMvc.perform(post("/predictions/add/1").flashAttr("inputPredictionDTO", predictionDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("add-prediction"))
                .andExpect(model().attributeHasFieldErrors("inputPredictionDTO", expectedErrors));

        verify(mockService, never()).savePrediction(any(), any());
    }

    @Test
    void testPostRequestAddPrediction_ServiceException() throws Exception {
        InputPredictionDTO predictionDTO = new InputPredictionDTO(2, 1);
        doThrow(new IllegalArgumentException("Deadline passed")).when(mockService).savePrediction(1L, predictionDTO);

        mockMvc.perform(post("/predictions/add/1").flashAttr("inputPredictionDTO", predictionDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("add-prediction"))
                .andExpect(model().attributeExists("errorMessage"));

        verify(mockService).savePrediction(1L, predictionDTO);
    }

    @Test
    void testGetEditPrediction() throws Exception {
        PredictionDTO predictionDTO = new PredictionDTO(1L, 2, 1);
        when(mockService.findPredictionByMatchId(1L)).thenReturn(Optional.of(predictionDTO));

        mockMvc.perform(get("/predictions/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-prediction"))
                .andExpect(model().attributeExists("inputPredictionDTO"))
                .andExpect(model().attribute("formAction", "predictions/edit/1"))
                .andExpect(model().attribute("submitLabel", "Edit Prediction"));

        verify(mockService).findPredictionByMatchId(1L);
    }

    @Test
    void testPostRequestValidEditPrediction() throws Exception {
        InputPredictionDTO predictionDTO = new InputPredictionDTO(2, 1);

        mockMvc.perform(post("/predictions/edit/1").flashAttr("inputPredictionDTO", predictionDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/predictions/matches"));

        verify(mockService).savePrediction(1L, predictionDTO);
    }

    @ParameterizedTest
    @MethodSource("invalidPredictionData")
    void testPostRequestInvalidEditPrediction(InputPredictionDTO predictionDTO, String[] expectedErrors) throws Exception {
        mockMvc.perform(post("/predictions/edit/1").flashAttr("inputPredictionDTO", predictionDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-prediction"))
                .andExpect(model().attributeHasFieldErrors("inputPredictionDTO", expectedErrors));

        verify(mockService, never()).savePrediction(any(), any());
    }
}
