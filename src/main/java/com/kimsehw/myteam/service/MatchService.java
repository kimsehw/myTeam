package com.kimsehw.myteam.service;

import com.kimsehw.myteam.domain.entity.match.Match;
import com.kimsehw.myteam.dto.match.MatchDto;
import com.kimsehw.myteam.dto.match.MatchSearchDto;
import com.kimsehw.myteam.repository.match.MatchRepository;
import java.util.List;
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

    public Page<MatchDto> getSearchedMatchDtoPages(MatchSearchDto matchSearchDto, List<Long> TeamIds,
                                                   Pageable pageable) {
        Page<Match> matchPage = matchRepository.findAllSearchedMatchPage(matchSearchDto, TeamIds, pageable);
        return matchPage.map(MatchDto::of);
    }
}
