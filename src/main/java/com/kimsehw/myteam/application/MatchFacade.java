package com.kimsehw.myteam.application;

import com.kimsehw.myteam.dto.match.MatchDto;
import com.kimsehw.myteam.dto.match.MatchSearchDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.service.MatchService;
import com.kimsehw.myteam.service.MemberService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchFacade {

    private final MemberService memberService;
    private final MatchService matchService;

    public Page<MatchDto> getSearchedMyMatchDtoPages(String email, Pageable pageable, MatchSearchDto matchSearchDto) {
        List<TeamInfoDto> myTeams = memberService.findMyTeamsInfoByEmail(email);
        List<Long> myTeamIds = myTeams.stream().map(TeamInfoDto::getTeamId).toList();
        return matchService.getSearchedMatchDtoPages(matchSearchDto, myTeamIds, pageable);
    }
}
