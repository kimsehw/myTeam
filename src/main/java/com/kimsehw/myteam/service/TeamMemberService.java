package com.kimsehw.myteam.service;

import com.kimsehw.myteam.constant.Position;
import com.kimsehw.myteam.constant.TeamRole;
import com.kimsehw.myteam.dto.team.TeamsDto;
import com.kimsehw.myteam.dto.teammember.TeamMemInviteFormDto;
import com.kimsehw.myteam.dto.teammember.TeamMemberDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.entity.TeamMember;
import com.kimsehw.myteam.entity.team.Team;
import com.kimsehw.myteam.repository.teammember.TeamMemberRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    public Page<TeamsDto> getTeamsDtoPage(Long memberId, Pageable pageable) {
        return teamMemberRepository.getTeamsDtoPage(memberId, pageable);
    }

    /**
     * 팀 생성 시 팀 초기 멤버(리더)를 등록
     *
     * @param team     소속 팀
     * @param member   팀원이 될 회원
     * @param teamRole 팀내 직책
     * @return teamMemberId
     */
    @Transactional
    public Long addInitialTeamMember(Team team, Member member, TeamRole teamRole) {
        TeamMember teamMember = TeamMember.createInitialTeamMember(team, member, teamRole);
        return teamMember.getId();
    }

    /**
     * 등번호를 배정하며 회원인 팀 멤버 등록
     *
     * @param team      소속 팀
     * @param member    팀원이 될 회원
     * @param teamRole  팀내 직책
     * @param playerNum 등번호
     * @return teamMemberId
     */
    @Transactional
    public Long addTeamMemberIn(Team team, Member member, TeamRole teamRole, int playerNum) {
        TeamMember teamMember = TeamMember.createTeamMember(team, member, teamRole, playerNum);
        return teamMember.getId();
    }

    /**
     * 비회원 팀 멤버 등록
     *
     * @param team
     * @param name
     * @param teamRole
     * @param playerNum
     * @param position
     */
    @Transactional
    public Long addTeamMemberIn(Team team, String name, TeamRole teamRole, Integer playerNum, Position position) {
        TeamMember teamMember = TeamMember.createNotUserTeamMember(team, name, teamRole, playerNum, position);
        return teamMember.getId();
    }

    public Page<TeamMemberDto> getTeamMemberDtoPagesOf(Long teamId, Pageable pageable) {
        List<TeamMemberDto> teamMemberDtos = teamMemberRepository.findAllTeamMemberDtoByTeamId(teamId, pageable);
        return new PageImpl<>(teamMemberDtos, pageable, teamMemberDtos.size());
    }

    public void validatePlayerNum(Long teamId, TeamMemInviteFormDto teamMemInviteFormDto, Map<String, String> errors) {
        Integer playerNum = teamMemInviteFormDto.getPlayerNum();
        if (playerNum == null) {
            errors.put("playerNum", "등 번호를 입력해주세요.");
            return;
        }

        TeamMember byPlayerNumAndTeamId = teamMemberRepository.findByPlayerNumAndTeamId(
                playerNum, teamId);
        if (byPlayerNumAndTeamId != null) {
            errors.put("playerNum", "중복된 등 번호 선수가 존재합니다.");
        }
    }
}
