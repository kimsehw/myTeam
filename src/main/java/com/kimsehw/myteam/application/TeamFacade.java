package com.kimsehw.myteam.application;

import com.kimsehw.myteam.constant.TeamRole;
import com.kimsehw.myteam.dto.TeamsDto;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.entity.Team;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamMemberService;
import com.kimsehw.myteam.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamFacade {

    private final TeamService teamService;
    private final MemberService memberService;
    private final TeamMemberService teamMemberService;

    public Long createTeam(String email, TeamFormDto teamFormDto) {
        Member member = memberService.findMemberByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException();
        }

        Team team = teamService.saveTeam(member, teamFormDto);
        teamMemberService.addTeamMemberIn(team, member, TeamRole.LEADER);
        return team.getId();
    }

    public List<TeamsDto> getMyTeams(String email) {
        Member member = memberService.findMemberByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException();
        }

        return null;
    }
}
