package com.kimsehw.myteam.application;

import com.kimsehw.myteam.dto.match.MatchListResponse;
import com.kimsehw.myteam.dto.match.MatchSearchDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.service.MatchService;
import com.kimsehw.myteam.service.MemberService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log
public class MatchFacade {

    private final MemberService memberService;
    private final MatchService matchService;

    public MatchListResponse getSearchedMyMatchListResponse(String email, Pageable pageable,
                                                            MatchSearchDto matchSearchDto) {
        List<TeamInfoDto> myTeams;
        try {
            myTeams = memberService.findMyTeamsInfoByEmail(email);
        } catch (EntityNotFoundException e) {
            return new MatchListResponse(null, new PageImpl<>(List.of(), pageable, 0));
        }
        return matchService.getSearchedMatchListResponse(myTeams, matchSearchDto, pageable);
    }
}
