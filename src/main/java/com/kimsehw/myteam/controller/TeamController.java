package com.kimsehw.myteam.controller;

import com.kimsehw.myteam.application.TeamFacade;
import com.kimsehw.myteam.constant.AgeRange;
import com.kimsehw.myteam.constant.Region;
import com.kimsehw.myteam.dto.TeamsDto;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log
@Controller
@RequiredArgsConstructor
public class TeamController {

    public static final int MAX_TEAM_SHOW = 10;
    private final TeamFacade teamFacade;

    @GetMapping("/teams/new")
    public String teamForm(Model model) {
        addRegionAndAgeRangeSelection(model);
        model.addAttribute("teamFormDto", new TeamFormDto());
        return "team/teamForm";
    }

    private void addRegionAndAgeRangeSelection(Model model) {
        model.addAttribute("regions", Region.values());
        model.addAttribute("ageRanges", AgeRange.values());
    }

    @PostMapping("/teams/new")
    public String newTeam(@Valid TeamFormDto teamFormDto, BindingResult bindingResult,
                          Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            addRegionAndAgeRangeSelection(model);
            return "team/teamForm";
        }

        try {
            String email = principal.getName();
            teamFacade.createTeam(email, teamFormDto);
        } catch (IllegalStateException e) {
            addAttributeWhenDuplicatedName(model, e);
            return "team/teamForm";
        } catch (RuntimeException e) {
            addRegionAndAgeRangeSelection(model);
            model.addAttribute("errorMessage", "팀 생성 중 에러 발생");
            return "team/teamForm";
        }

        return "redirect:/";
    }

    private void addAttributeWhenDuplicatedName(Model model, IllegalStateException e) {
        addRegionAndAgeRangeSelection(model);
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute("resetName", true);
    }

    @GetMapping(value = {"/teams"})
    public String teamView(Model model, Principal principal,
                           @RequestParam(value = "page", defaultValue = "0") int page) {
        String email = principal.getName();
        Pageable pageable = PageRequest.of(page, MAX_TEAM_SHOW);
        Page<TeamsDto> teams = teamFacade.getMyTeams(email, pageable);
        log.info(String.valueOf(teams.getContent()));
        model.addAttribute("teams", teams);
        model.addAttribute("maxPage", MAX_TEAM_SHOW);
        model.addAttribute("page", pageable.getPageNumber());
        return "team/teamList";
    }
}
