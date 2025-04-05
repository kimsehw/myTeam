package com.kimsehw.myteam.repository.post;

import com.kimsehw.myteam.constant.post.PostType;
import com.kimsehw.myteam.constant.search.SearchDateType;
import com.kimsehw.myteam.constant.search.SearchType;
import com.kimsehw.myteam.domain.entity.post.Post;
import com.kimsehw.myteam.domain.entity.post.QChat;
import com.kimsehw.myteam.domain.entity.post.QPost;
import com.kimsehw.myteam.domain.entity.team.QTeam;
import com.kimsehw.myteam.domain.entity.team.QTeamLogo;
import com.kimsehw.myteam.dto.post.PostDetailDto;
import com.kimsehw.myteam.dto.post.PostDto;
import com.kimsehw.myteam.dto.post.PostSearchDto;
import com.kimsehw.myteam.dto.post.QPostDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<PostDto> findAllPostDto(PostSearchDto postSearchDto, Pageable pageable) {
        QPost post = QPost.post;
        QTeam team = QTeam.team;
        List<PostDto> postDtos = queryFactory.select(
                        new QPostDto(post.id, post.regTime, post.title, post.postType, post.createdBy, team.teamName)
                )
                .from(post)
                .leftJoin(post.team, team)
                .where(
                        searchPostTypeEq(postSearchDto.getPostType()),
                        regDtsBetween(postSearchDto.getFromDate(), postSearchDto.getToDate(),
                                postSearchDto.getSearchDateType()),
                        searchByLike(postSearchDto.getSearchType(), postSearchDto.getSearchQuery())
                )
                .orderBy(post.regTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(post.count())
                .from(post)
                .leftJoin(post.team, team)
                .where(
                        searchPostTypeEq(postSearchDto.getPostType()),
                        regDtsBetween(postSearchDto.getFromDate(), postSearchDto.getToDate(),
                                postSearchDto.getSearchDateType()),
                        searchByLike(postSearchDto.getSearchType(), postSearchDto.getSearchQuery())
                )
                .fetchOne();
        if (total == null) {
            total = 0L;
        }

        return new PageImpl<>(postDtos, pageable, total);
    }

    private BooleanExpression searchByLike(SearchType searchType, String searchQuery) {
        if (searchQuery == null || searchQuery.isBlank()) {
            return null;
        }
        if (searchType == SearchType.TITLE) {
            return QPost.post.title.like("%" + searchQuery + "%");
        }
        if (searchType == SearchType.CONTENT) {
            return QPost.post.detail.like("%" + searchQuery + "%");
        }
        if (searchType == SearchType.WRITER) {
            return QPost.post.createdBy.like("%" + searchQuery + "%");
        }
        if (searchType == SearchType.TEAM_NAME) {
            return QPost.post.team.teamName.like("%" + searchQuery + "%");
        }
        return null;
    }

    private BooleanExpression searchPostTypeEq(PostType postType) {
        if (postType == null) {
            return null;
        }
        return QPost.post.postType.eq(postType);
    }

    private BooleanExpression regDtsBetween(String fromDate, String toDate, SearchDateType searchDateType) {

        if ((fromDate == null || fromDate.isBlank()) && (toDate == null || toDate.isBlank())
                && searchDateType == null) {
            return null;
        }
        if (searchDateType != null) {
            return regDtsAfter(searchDateType);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fromDateTime = getFromDateTime(fromDate, formatter);
        LocalDateTime toDateTime = getToDateTime(toDate, formatter);

        if (fromDateTime != null && toDateTime != null) {
            return QPost.post.regTime.between(fromDateTime, toDateTime);
        }
        if (fromDateTime != null) {
            return QPost.post.regTime.goe(fromDateTime);
        }
        if (toDateTime != null) {
            return QPost.post.regTime.loe(toDateTime);
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

    private BooleanExpression regDtsAfter(SearchDateType searchDateType) {
        LocalDateTime now = LocalDateTime.now();

        if (SearchDateType.ALL == searchDateType) {
            return null;
        }
        if (SearchDateType.TODAY == searchDateType) {
            now = now.minusDays(1);
        }
        if (SearchDateType.ONE_WEEK == searchDateType) {
            now = now.minusWeeks(1);
        }
        if (SearchDateType.ONE_MONTH == searchDateType) {
            now = now.minusMonths(1);
        }
        if (SearchDateType.SIX_MONTH == searchDateType) {
            now = now.minusMonths(6);
        }
        if (SearchDateType.ONE_YEAR == searchDateType) {
            now = now.minusMonths(12);
        }

        return QPost.post.regTime.goe(now);
    }

    @Override
    public PostDetailDto findPostDetailDtoById(Long postId) {
        QPost post = QPost.post;
        QTeam team = QTeam.team;
        QTeamLogo teamLogo = QTeamLogo.teamLogo;

        return queryFactory
                .select(Projections.constructor(
                        PostDetailDto.class,
                        post.id, post.title, post.detail, post.createdBy, post.regTime,
                        team.id.as("teamId"), teamLogo.imgUrl.as("logoUrl"),
                        team.teamName, team.region, team.ageRange)
                )
                .from(post)
                .leftJoin(post.team, team)
                .leftJoin(team.teamLogo, teamLogo)
                .where(post.id.eq(postId))
                .fetchOne();
    }

    @Override
    public Post findAllWithChatAndChildChatsByIdUseFetch(Long postId) {
        QPost post = QPost.post;
        QChat chat = QChat.chat;
        QChat childChat = new QChat("childChat");
        return queryFactory
                .selectFrom(post)
                .distinct()
                .leftJoin(post.chats, chat).fetchJoin()
                .leftJoin(chat.childChats, childChat)
                .where(post.id.eq(postId))
                .fetchOne();
    }
}
