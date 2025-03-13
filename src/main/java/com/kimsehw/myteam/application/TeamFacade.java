package com.kimsehw.myteam.application;

import com.kimsehw.myteam.dto.TeamFormDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamFacade {

    private final TeamService teamService;
    private final MemberService memberService;


    public Long createTeam(String email, TeamFormDto teamFormDto) {
        Member member = memberService.findMemberByEmail(email);
        if (member == null) {
            throw new EntityNotFoundException();
        }
        return teamService.saveTeam(member, teamFormDto);
    }
}
