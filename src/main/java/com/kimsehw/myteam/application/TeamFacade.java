package com.kimsehw.myteam.application;

import com.kimsehw.myteam.constant.TeamRole;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.dto.team.TeamsDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.entity.team.Team;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamMemberService;
import com.kimsehw.myteam.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TeamFacade {

    private final TeamService teamService;
    private final MemberService memberService;
    private final TeamMemberService teamMemberService;

    public Long createTeam(String email, TeamFormDto teamFormDto, MultipartFile teamLogoFile) {
        Member member = memberService.findMemberByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException();
        }

        Team team = teamService.saveTeam(member, teamFormDto, teamLogoFile);
        teamMemberService.addTeamMemberIn(team, member, TeamRole.LEADER);
        return team.getId();
    }

    public Page<TeamsDto> getMyTeams(String email, Pageable pageable) {
        Member member = memberService.findMemberByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException();
        }
        return teamMemberService.getTeamsDtoPage(member.getId(), pageable);
    }
}
