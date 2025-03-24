package com.kimsehw.myteam.controller;

import com.kimsehw.myteam.application.TeamFacade;
import com.kimsehw.myteam.dto.teammember.TeamMemberDto;
import com.kimsehw.myteam.service.TeamMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Log
@Controller
@RequiredArgsConstructor
public class TeamMemberController {

    public static final int MAX_TEAM_MEM_SHOW = 20;

    private final TeamFacade teamFacade;
    private final TeamMemberService teamMemberService;

    @GetMapping("/teams/{teamId}/team-members")
    public String teamMembers(Model model, @PathVariable("teamId") Long teamId,
                              @RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, MAX_TEAM_MEM_SHOW);
        Page<TeamMemberDto> teamMemberDtos = teamMemberService.getTeamMemberDtoPagesOf(teamId, pageable);

        model.addAttribute("teamMembers", teamMemberDtos);
        model.addAttribute("maxPage", MAX_TEAM_MEM_SHOW);
        model.addAttribute("page", pageable.getPageNumber());
        return "team/teamMem/teamMemList";
    }
}
