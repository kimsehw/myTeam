package com.kimsehw.myteam.controller;

import com.kimsehw.myteam.application.TeamFacade;
import com.kimsehw.myteam.dto.TeamFormDto;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamFacade teamFacade;

    @GetMapping("/team/new")
    public String teamForm(Model model) {
        model.addAttribute("teamFormDto", new TeamFormDto());
        return "team/teamForm";
    }

    @PostMapping("/team/new")
    public String newTeam(@Valid TeamFormDto teamFormDto, Model model,
                          Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return "team/teamForm";
        }

        try {
            String email = principal.getName();
            teamFacade.createTeam(email, teamFormDto);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "팀 생성 중 에러 발생");
            return "team/teamForm";
        }

        return "redirect:/";
    }
}
