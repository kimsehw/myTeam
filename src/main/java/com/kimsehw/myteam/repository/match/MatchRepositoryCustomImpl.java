package com.kimsehw.myteam.repository.match;

import com.kimsehw.myteam.constant.search.SearchDateType;
import com.kimsehw.myteam.domain.entity.match.Match;
import com.kimsehw.myteam.domain.entity.match.QMatch;
import com.kimsehw.myteam.domain.utill.DateTimeUtil;
import com.kimsehw.myteam.dto.match.MatchSearchDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Log
public class MatchRepositoryCustomImpl implements MatchRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MatchRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Match> findAllSearchedMatchPage(MatchSearchDto matchSearchDto, Set<Long> TeamIds, Pageable pageable) {
        QMatch match = QMatch.match;
//        log.info(matchSearchDto.toString());
        List<Match> matches = queryFactory.select(match)
                .from(match)
                .where(
                        match.myTeam.id.in(TeamIds), searchIsDoneEq(matchSearchDto, match),
                        matchDateBetween(matchSearchDto.getFromDate(), matchSearchDto.getToDate(),
                                matchSearchDto.getSearchDateType(), match),
                        searchTeamEq(matchSearchDto.getTeamId(), match)
                )
                .orderBy(match.matchDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(match.count())
                .from(match)
                .where(
                        match.myTeam.id.in(TeamIds), searchIsDoneEq(matchSearchDto, match),
                        matchDateBetween(matchSearchDto.getToDate(), matchSearchDto.getToDate(),
                                matchSearchDto.getSearchDateType(), match)
                )
                .fetchOne();
        return new PageImpl<>(matches, pageable, total == null ? 0L : total);
    }

    private BooleanExpression searchTeamEq(Long teamId, QMatch match) {
        if (teamId == null) {
            return null;
        }
        return match.myTeam.id.eq(teamId);
    }

    private static BooleanExpression searchIsDoneEq(MatchSearchDto matchSearchDto, QMatch match) {
        if (matchSearchDto.getIsDone() == null) {
            return null;
        }
        return match.isDone.eq(matchSearchDto.getIsDone());
    }

    private BooleanExpression matchDateBetween(String fromDate, String toDate, SearchDateType searchDateType,
                                               QMatch match) {
        if ((fromDate == null || fromDate.isBlank()) && (toDate == null || toDate.isBlank())
                && searchDateType == null) {
            return null;
        }
        if (searchDateType != null) {
            return match.matchDate.goe(DateTimeUtil.getBeforeDateTypeOf(searchDateType));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fromDateTime = getFromDateTime(fromDate, formatter);
        LocalDateTime toDateTime = getToDateTime(toDate, formatter);

        if (fromDateTime != null && toDateTime != null) {
            return match.matchDate.between(fromDateTime, toDateTime);
        }
        if (fromDateTime != null) {
            return match.matchDate.goe(fromDateTime);
        }
        if (toDateTime != null) {
            return match.matchDate.loe(toDateTime);
        }

        return null;
    }

    private static LocalDateTime getToDateTime(String toDate, DateTimeFormatter formatter) {
        if (toDate != null && !toDate.isBlank()) {
            return LocalDate.parse(toDate, formatter).atTime(23, 59, 59);
        }
        return null;
    }

    private static LocalDateTime getFromDateTime(String fromDate, DateTimeFormatter formatter) {
        if (fromDate != null && !fromDate.isBlank()) {
            return LocalDate.parse(fromDate, formatter).atStartOfDay();
        }
        return null;
    }
}
