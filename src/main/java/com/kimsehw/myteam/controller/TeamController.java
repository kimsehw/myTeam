package com.kimsehw.myteam.controller;

import com.kimsehw.myteam.application.TeamFacade;
import com.kimsehw.myteam.constant.AgeRange;
import com.kimsehw.myteam.constant.Region;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Log
@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamFacade teamFacade;

    @GetMapping("/team/new")
    public String teamForm(Model model) {
        addRegionAndAgeRangeSelection(model);
        model.addAttribute("teamFormDto", new TeamFormDto());
        return "team/teamForm";
    }

    private void addRegionAndAgeRangeSelection(Model model) {
        model.addAttribute("regions", Region.values());
        model.addAttribute("ageRanges", AgeRange.values());
    }

    @PostMapping("/team/new")
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

    @GetMapping("/team/{memberId}")
    public String teamList(Model model, @PathVariable("memberId") Long memberId) {
        return null;
    }
}
