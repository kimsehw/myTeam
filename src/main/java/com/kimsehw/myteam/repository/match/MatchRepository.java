package com.kimsehw.myteam.repository.match;

import com.kimsehw.myteam.domain.entity.match.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long>, MatchRepositoryCustom {
}
