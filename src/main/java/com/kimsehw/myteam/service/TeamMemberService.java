package com.kimsehw.myteam.service;

import com.kimsehw.myteam.constant.TeamRole;
import com.kimsehw.myteam.dto.team.TeamsDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.entity.TeamMember;
import com.kimsehw.myteam.entity.team.Team;
import com.kimsehw.myteam.repository.teammember.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
     * 회원이 팀 멤버로 소속 됨
     *
     * @param team     소속 팀
     * @param member   팀원이 될 회원
     * @param teamRole 팀내 직책
     * @return teamMemberId
     */
    @Transactional
    public Long addTeamMemberIn(Team team, Member member, TeamRole teamRole) {
        TeamMember teamMember = TeamMember.createTeamMember(team, member, teamRole);
        return teamMember.getId();
    }
}
