package com.kimsehw.myteam.application;

import com.kimsehw.myteam.domain.FieldError;
import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.TeamMember;
import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.dto.match.MatchDto;
import com.kimsehw.myteam.dto.match.MatchSearchDto;
import com.kimsehw.myteam.dto.team.MatchTeamInfoDto;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.dto.team.TeamsDto;
import com.kimsehw.myteam.exception.FieldErrorException;
import com.kimsehw.myteam.service.MatchService;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamMemberService;
import com.kimsehw.myteam.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Log
public class TeamFacade {

    private final TeamService teamService;
    private final MemberService memberService;
    private final TeamMemberService teamMemberService;
    private final MatchService matchService;

    public Long createTeam(String email, TeamFormDto teamFormDto, MultipartFile teamLogoFile) {
        Member member = memberService.getMemberBy(email);
        if (member == null) {
            throw new EntityNotFoundException();
        }

        TeamMember leader = TeamMember.createInitialTeamMember(member);
        Team team = teamService.saveTeam(leader, teamFormDto, teamLogoFile);
        return team.getId();
    }

    public Page<TeamsDto> getMyTeams(String email, Pageable pageable) {
        return memberService.getTeamsDtoPage(email, pageable);
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
        Member member = memberService.getMemberBy(email);
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

    /**
     * 팀의 일정을 조회합니다.
     *
     * @param teamId
     * @param pageable
     * @param matchSearchDto
     * @return Page<MatchDto>
     */
    public Page<MatchDto> getSearchedTeamMatchList(Long teamId, Pageable pageable, MatchSearchDto matchSearchDto) {
        return matchService.getSearchedMatchDtoPages(matchSearchDto, Set.of(teamId), pageable);
    }

    public List<MatchTeamInfoDto> getMatchingTeamsByName(String searchTeamName) {
        if (searchTeamName.isBlank()) {
            throw new FieldErrorException(FieldError.of("searchTeamName", "팀명을 입력해주세요."));
        }
        List<Team> teams = teamService.findAllByTeamNameWithLeaderInfo(searchTeamName);
        if (teams == null || teams.isEmpty()) {
            throw new FieldErrorException(FieldError.of("searchTeamName", "존재하지 않는 팀명입니다. 팀명을 확인해주세요."));
        }
        return teams.stream()
                .map(MatchTeamInfoDto::of)
                .toList();
    }
}