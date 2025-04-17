package com.kimsehw.myteam.repository.member;

import com.kimsehw.myteam.domain.entity.QMember;
import com.kimsehw.myteam.domain.entity.QTeamMember;
import com.kimsehw.myteam.domain.entity.team.QTeam;
import com.kimsehw.myteam.dto.team.QTeamsDto;
import com.kimsehw.myteam.dto.team.TeamsDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public MemberRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<TeamsDto> findMyTeamsDtoPageByEmail(String email, Pageable pageable) {
        QMember member = QMember.member;
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
                .from(member)
                .leftJoin(member.myTeams, teamMember)
                .join(teamMember.team, team)
                .where(member.email.eq(email))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(member.count())
                .from(member)
                .leftJoin(member.myTeams, teamMember)
                .join(teamMember.team, team)
                .where(member.email.eq(email))
                .fetchOne();
        if (total == null) {
            total = 0L;
        }
        return new PageImpl<>(teamsDtos, pageable, total);
    }
}
