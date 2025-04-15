package com.kimsehw.myteam.repository.match;

import com.kimsehw.myteam.domain.entity.match.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MatchRepository extends JpaRepository<Match, Long>, MatchRepositoryCustom {

    @Query("select m from Match m"
            + " left join fetch m.myTeam mt"
            + " left join fetch m.opposingTeam ot"
            + " left join fetch m.matchMemberRecords r"
            + " where m.id = :matchId")
    Match findByIdFetchAll(Long matchId);
}
