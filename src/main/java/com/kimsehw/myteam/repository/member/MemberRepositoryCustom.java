package com.kimsehw.myteam.repository.member;

import com.kimsehw.myteam.dto.team.TeamsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    Page<TeamsDto> findMyTeamsDtoPageByEmail(String email, Pageable pageable);
}
