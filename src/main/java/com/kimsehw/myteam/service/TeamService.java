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
        Team team = Team.createTeam(teamFormDto, member);
        teamRepository.save(team);
        return team.getId();
    }
}
