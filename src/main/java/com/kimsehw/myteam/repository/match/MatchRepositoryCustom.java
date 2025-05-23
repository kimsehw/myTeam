package com.kimsehw.myteam.repository.match;

import com.kimsehw.myteam.domain.entity.match.Match;
import com.kimsehw.myteam.dto.match.MatchSearchDto;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchRepositoryCustom {

    Page<Match> findAllSearchedMatchPage(MatchSearchDto matchSearchDto, Set<Long> myTeamIds, Pageable pageable);
}
