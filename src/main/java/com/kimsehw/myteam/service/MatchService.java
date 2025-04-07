package com.kimsehw.myteam.service;

import com.kimsehw.myteam.domain.entity.match.Match;
import com.kimsehw.myteam.dto.match.MatchDto;
import com.kimsehw.myteam.dto.match.MatchListResponse;
import com.kimsehw.myteam.dto.match.MatchSearchDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.repository.match.MatchRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    public Page<MatchDto> getSearchedMatchDtoPages(MatchSearchDto matchSearchDto, Set<Long> TeamIds,
                                                   Pageable pageable) {
        Page<Match> matchPage = matchRepository.findAllSearchedMatchPage(matchSearchDto, TeamIds, pageable);
        return matchPage.map(MatchDto::of);
    }

    public MatchListResponse getSearchedMatchListResponse(List<TeamInfoDto> myTeams, MatchSearchDto matchSearchDto,
                                                          Pageable pageable) {
        Map<Long, String> myTeamIdsAndNames = myTeams.stream()
                .collect(Collectors.toMap(TeamInfoDto::getTeamId, TeamInfoDto::getTeamName));
        Set<Long> myTeamIds = myTeamIdsAndNames.keySet();
        Page<MatchDto> matches = getSearchedMatchDtoPages(matchSearchDto, myTeamIds, pageable);
        return new MatchListResponse(myTeamIdsAndNames, matches);
    }
}
