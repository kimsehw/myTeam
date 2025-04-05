package com.kimsehw.myteam.repository.teammember;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class TeamMemberRepositoryCustomImpl implements TeamMemberRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public TeamMemberRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

}
