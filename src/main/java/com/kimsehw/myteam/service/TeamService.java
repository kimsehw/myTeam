package com.kimsehw.myteam.service;

import com.kimsehw.myteam.dto.TeamFormDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.entity.Team;
import com.kimsehw.myteam.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public Long saveTeam(Member member, TeamFormDto teamFormDto) {
        if (teamRepository.findByMemberIdAndTeamName(member.getId(), teamFormDto.getTeamName()) != null) {
            throw new IllegalStateException("같은 이름의 팀이 존재합니다. 같은 이름의 팀은 한 개 이상 만들 수 없습니다.");
        }

        Team team = Team.createTeam(teamFormDto, member);
        teamRepository.save(team);
        return team.getId();
    }
}
