package com.kimsehw.myteam.service;

import com.kimsehw.myteam.dto.TeamsDto;
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
}
