package com.kimsehw.myteam.repository.teammember;

import com.kimsehw.myteam.dto.QTeamsDto;
import com.kimsehw.myteam.dto.TeamsDto;
import com.kimsehw.myteam.entity.QTeam;
import com.kimsehw.myteam.entity.QTeamMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class TeamMemberRepositoryCustomImpl implements TeamMemberRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public TeamMemberRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    /*
     * 팀 목록 조회
     * 나중에 검색 조건 추가
     * */
    @Override
    public Page<TeamsDto> getTeamsDtoPage(Long memberId, Pageable pageable) {
        QTeamMember teamMember = QTeamMember.teamMember;
        QTeam team = QTeam.team;

        List<TeamsDto> teamsDtos = queryFactory.select(
                        new QTeamsDto(
                                teamMember.id,
                                team.id,
                                teamMember.teamRole,
                                team.teamName,
                                team.region
                        )
                )
                .from(teamMember)
                .where(teamMember.member.id.eq(memberId))
                .join(teamMember.team, team)
                .orderBy(teamMember.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(teamsDtos, pageable, teamsDtos.size());
    }
}
