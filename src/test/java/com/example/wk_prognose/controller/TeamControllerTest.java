package com.example.wk_prognose.controller;

import com.example.wk_prognose.dto.request.CreateTeamDTO;
import com.example.wk_prognose.dto.request.JoinTeamDTO;
import com.example.wk_prognose.dto.response.TeamDetailDTO;
import com.example.wk_prognose.dto.response.TeamMemberDTO;
import com.example.wk_prognose.dto.response.TeamRankingDTO;
import com.example.wk_prognose.service.CurrentUserService;
import com.example.wk_prognose.service.RankingService;
import com.example.wk_prognose.service.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

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

@WebMvcTest(TeamController.class)
@AutoConfigureMockMvc(addFilters = false)
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeamService mockService;

    @MockitoBean
    private RankingService rankingService;

    @MockitoBean
    private CurrentUserService currentUserService;

    @Test
    void testGetMyTeam_UserHasTeam() throws Exception {
        when(mockService.currentUserHasTeam()).thenReturn(true);
        when(mockService.findMyTeamLink()).thenReturn("/teams/1");

        mockMvc.perform(get("/teams"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teams/1"));

        verify(mockService).currentUserHasTeam();
        verify(mockService).findMyTeamLink();
    }

    @Test
    void testGetMyTeam_UserHasNoTeam() throws Exception {
        when(mockService.currentUserHasTeam()).thenReturn(false);

        mockMvc.perform(get("/teams"))
                .andExpect(status().isOk())
                .andExpect(view().name("team-empty"));

        verify(mockService).currentUserHasTeam();
    }

    @Test
    void testGetTeam() throws Exception {
        TeamDetailDTO expectedTeam = new TeamDetailDTO(1L, "Team Test", "Nicolas Test",
                1, 10, List.of(), "ABC12345", true);
        List<TeamMemberDTO> expectedMembers = List.of(new TeamMemberDTO(1L, "Nicolas Test", 10, true));
        when(mockService.findTeamById(1L)).thenReturn(Optional.of(expectedTeam));
        when(rankingService.findRankedMembersForTeam(1L)).thenReturn(expectedMembers);
        when(rankingService.calculateTotalScoreForTeam(1L)).thenReturn(10);

        mockMvc.perform(get("/teams/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("team-detail"))
                .andExpect(model().attribute("teamDetailDTO", expectedTeam))
                .andExpect(model().attribute("rankedMembers", expectedMembers))
                .andExpect(model().attribute("teamTotalScore", 10));

        verify(mockService).findTeamById(1L);
        verify(rankingService).findRankedMembersForTeam(1L);
        verify(rankingService).calculateTotalScoreForTeam(1L);
    }

    @Test
    void testGetTop10Teams() throws Exception {
        List<TeamRankingDTO> expectedTeams = List.of(new TeamRankingDTO(1L, "Team Test", 10, 1));
        when(rankingService.findTop10Teams()).thenReturn(expectedTeams);

        mockMvc.perform(get("/teams/ranking"))
                .andExpect(status().isOk())
                .andExpect(view().name("top-teams"))
                .andExpect(model().attribute("topTeams", expectedTeams));

        verify(rankingService).findTop10Teams();
    }

    @Test
    void testGetCreateTeam() throws Exception {
        mockMvc.perform(get("/teams/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("create-team"))
                .andExpect(model().attributeExists("createTeamDTO"));
    }

    @Test
    void testPostRequestValidCreateTeam() throws Exception {
        CreateTeamDTO createTeamDTO = new CreateTeamDTO("Team Test");
        when(mockService.findMyTeamLink()).thenReturn("/teams/1");

        mockMvc.perform(post("/teams/create").flashAttr("createTeamDTO", createTeamDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teams/1"));

        verify(mockService).createTeam(any(CreateTeamDTO.class));
        verify(mockService).findMyTeamLink();
    }

    @Test
    void testPostRequestInvalidCreateTeam() throws Exception {
        CreateTeamDTO createTeamDTO = new CreateTeamDTO("");

        mockMvc.perform(post("/teams/create").flashAttr("createTeamDTO", createTeamDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("create-team"))
                .andExpect(model().attributeHasFieldErrors("createTeamDTO", "name"));

        verify(mockService, never()).createTeam(any());
    }

    @Test
    void testPostRequestCreateTeam_ServiceException() throws Exception {
        CreateTeamDTO createTeamDTO = new CreateTeamDTO("Team Test");
        doThrow(new IllegalArgumentException("Already in team")).when(mockService).createTeam(any(CreateTeamDTO.class));

        mockMvc.perform(post("/teams/create").flashAttr("createTeamDTO", createTeamDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("create-team"));

        verify(mockService).createTeam(any(CreateTeamDTO.class));
        verify(mockService, never()).findMyTeamLink();
    }

    @Test
    void testGetJoinTeam() throws Exception {
        mockMvc.perform(get("/teams/join"))
                .andExpect(status().isOk())
                .andExpect(view().name("join-team"))
                .andExpect(model().attributeExists("joinTeamDTO"));
    }

    @Test
    void testPostRequestValidJoinTeam() throws Exception {
        JoinTeamDTO joinTeamDTO = new JoinTeamDTO("ABC12345");
        when(mockService.findMyTeamLink()).thenReturn("/teams/1");

        mockMvc.perform(post("/teams/join").flashAttr("joinTeamDTO", joinTeamDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teams/1"));

        verify(mockService).joinTeam(any(JoinTeamDTO.class));
        verify(mockService).findMyTeamLink();
    }

    @Test
    void testPostRequestInvalidJoinTeam() throws Exception {
        JoinTeamDTO joinTeamDTO = new JoinTeamDTO("");

        mockMvc.perform(post("/teams/join").flashAttr("joinTeamDTO", joinTeamDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("join-team"))
                .andExpect(model().attributeHasFieldErrors("joinTeamDTO", "inviteCode"));

        verify(mockService, never()).joinTeam(any());
    }

    @Test
    void testRegenerateInviteCode() throws Exception {
        mockMvc.perform(post("/teams/1/regenerate-invite-code"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teams/1"));

        verify(mockService).regenerateInviteCode(1L);
    }

    @Test
    void testRemoveMemberFromTeam() throws Exception {
        mockMvc.perform(post("/teams/1/members/2/remove"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teams/1"));

        verify(mockService).removeMember(1L, 2L);
    }
}
