package com.kimsehw.myteam.application;

import com.kimsehw.myteam.constant.teammember.TeamRole;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
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
        teamMemberService.addInitialTeamMember(team, member, TeamRole.LEADER);
        return team.getId();
    }

    public Page<TeamsDto> getMyTeams(String email, Pageable pageable) {
        Member member = memberService.findMemberByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException();
        }
        return teamMemberService.getTeamsDtoPage(member.getId(), pageable);
    }

    public TeamInfoDto getTeamInfoOf(Long teamId) {
        return teamService.getTeamInfoDtoOf(teamId);
    }

    /**
     * 팀 정보를 업데이트 합니다.
     *
     * @param email              회원 아이디
     * @param updateTeamInfoDto  업데이트 정보
     * @param updateTeamLogoFile 업데이트 로고 파일
     */
    public void updateTeam(String email, TeamInfoDto updateTeamInfoDto, MultipartFile updateTeamLogoFile) {
        Member member = memberService.findMemberByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException();
        }

        teamService.updateTeam(member, updateTeamInfoDto, updateTeamLogoFile);
    }

    public void deleteTeam(Long teamId, String logoUrl) {
        teamService.deleteTeam(teamId, logoUrl);
    }

    public String getTeamName(Long teamId) {
        return teamService.getTeamName(teamId);
    }
}