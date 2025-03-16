package com.kimsehw.myteam.repository.teammember;

import com.kimsehw.myteam.dto.TeamsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamMemberRepositoryCustom {

    Page<TeamsDto> getTeamsDtoPage(Long memberId, Pageable pageable);
}
