package com.kimsehw.myteam.controller.team;

import com.kimsehw.myteam.application.TeamFacade;
import com.kimsehw.myteam.application.TeamMemFacade;
import com.kimsehw.myteam.constant.search.SearchDateType;
import com.kimsehw.myteam.constant.team.AgeRange;
import com.kimsehw.myteam.constant.team.Region;
import com.kimsehw.myteam.dto.match.MatchDto;
import com.kimsehw.myteam.dto.match.MatchSearchDto;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.dto.team.TeamsDto;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Log
@Controller
@RequiredArgsConstructor
public class TeamController {

    public static final int MAX_TEAM_SHOW = 1;
    public static final int MAX_MATCH_SHOW = 10;

    private final TeamFacade teamFacade;
    private final TeamMemFacade teamMemFacade;

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
                          Model model, Principal principal, @RequestParam("teamLogoFile") MultipartFile teamLogoFile) {
        if (bindingResult.hasErrors()) {
            addRegionAndAgeRangeSelection(model);
            return "team/teamForm";
        }

        try {
            String email = principal.getName();
            teamFacade.createTeam(email, teamFormDto, teamLogoFile);
        } catch (IllegalStateException e) {
            addAttributeWhenDuplicatedName(model, e, "resetName");
            return "team/teamForm";
        } catch (RuntimeException e) {
            addRegionAndAgeRangeSelection(model);
            log.info(e.getMessage());
            model.addAttribute("errorMessage", "팀 생성 중 에러 발생");
            return "team/teamForm";
        }

        return "redirect:/";
    }

    private void addAttributeWhenDuplicatedName(Model model, IllegalStateException e, String trueAttributeType) {
        addRegionAndAgeRangeSelection(model);
        model.addAttribute("errorMessage", e.getMessage());
        model.addAttribute(trueAttributeType, true);
    }

    @GetMapping(value = {"/teams"})
    public String teamView(Model model, Principal principal,
                           @RequestParam(value = "page", defaultValue = "0") int page) {
        String email = principal.getName();
        Pageable pageable = PageRequest.of(page, MAX_TEAM_SHOW);
        Page<TeamsDto> teams = teamFacade.getMyTeams(email, pageable);

        model.addAttribute("teams", teams);
        addAttributeOfPage(model, MAX_TEAM_SHOW, pageable);
        return "team/teamList";
    }

    @GetMapping("/teams/{teamId}")
    public String teamDetail(Model model, @PathVariable("teamId") Long teamId) {
        TeamInfoDto teamInfoDto = teamFacade.getTeamInfoOf(teamId);
        model.addAttribute("teamInfoDto", teamInfoDto);
        addRegionAndAgeRangeSelection(model);
        return "team/teamDetail";
    }

    @PostMapping("/teams/{teamId}")
    public String teamUpdate(@ModelAttribute("teamInfoDto") @Valid TeamInfoDto updateTeamInfoDto,
                             BindingResult bindingResult, Model model, Principal principal,
                             @RequestParam("teamLogoFile") MultipartFile updateTeamLogoFile,
                             @PathVariable("teamId") Long teamId) {

        if (bindingResult.hasErrors()) {
            addAttributeWhenUpdateNotDone(model);
            keepOriginTeamName(model, teamId);
            return "team/teamDetail";
        }

        try {
            String email = principal.getName();
            teamFacade.updateTeam(email, updateTeamInfoDto, updateTeamLogoFile);
        } catch (IllegalStateException e) {
            addAttributeWhenDuplicatedName(model, e, "updateNotDone");
            model.addAttribute("nameDuplicateError", e.getMessage());
            keepOriginTeamName(model, teamId);
            return "team/teamDetail";
        } catch (RuntimeException e) {
            addAttributeWhenUpdateNotDone(model);
            log.info(e.getMessage());
            model.addAttribute("errorMessage", "팀 변경 중 에러 발생");
            return "team/teamDetail";
        }
        return "redirect:/teams/" + teamId;
    }

    private void keepOriginTeamName(Model model, Long teamId) {
        String teamName = teamFacade.getTeamName(teamId);
        model.addAttribute("originTeamName", teamName);
    }

    private void addAttributeWhenUpdateNotDone(Model model) {
        addRegionAndAgeRangeSelection(model);
        model.addAttribute("updateNotDone", true);
    }

    @GetMapping("/teams/{teamId}/matches")
    public String matchView(Model model, @PathVariable("teamId") Long teamId, HttpSession session, Principal principal,
                            @RequestParam(value = "page", defaultValue = "0") int page,
                            @ModelAttribute("matchSearch") MatchSearchDto matchSearchDto) {
        Pageable pageable = PageRequest.of(page, MAX_MATCH_SHOW);
        Page<MatchDto> matches = teamFacade.getSearchedTeamMatchList(teamId, pageable, matchSearchDto);
        boolean manageTeam = teamMemFacade.isAuthorizeToManageTeam(principal, teamId);

        session.setAttribute("currentViewTeamId", teamId);
        model.addAttribute("matches", matches);
        model.addAttribute("teamId", teamId);
        model.addAttribute("manageTeam", manageTeam);
        model.addAttribute("myListView", false);
        model.addAttribute("searchDateTypes", SearchDateType.values());
        addAttributeOfPage(model, MAX_MATCH_SHOW, pageable);
        return "team/teamMatchList";
    }

    private static void addAttributeOfPage(Model model, int maxMatchShow, Pageable pageable) {
        model.addAttribute("maxPage", maxMatchShow);
        model.addAttribute("page", pageable.getPageNumber());
    }
}
