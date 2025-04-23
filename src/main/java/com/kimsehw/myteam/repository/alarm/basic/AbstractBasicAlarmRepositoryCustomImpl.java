package com.kimsehw.myteam.repository.alarm.basic;

import com.kimsehw.myteam.domain.entity.QMember;
import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.domain.entity.alarm.QAlarm;
import com.kimsehw.myteam.domain.entity.alarm.QMatchInviteAlarm;
import com.kimsehw.myteam.domain.entity.alarm.QTeamMemInviteAlarm;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

public abstract class AbstractBasicAlarmRepositoryCustomImpl<T extends Alarm> {

    protected final JPAQueryFactory queryFactory;

    public AbstractBasicAlarmRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    public BooleanExpression searchIsRead(Boolean isRead, QAlarm alarm) {
        if (isRead == null) {
            return null;
        }
        return alarm.isRead.eq(isRead);
    }

    public BooleanExpression searchIsRead(Boolean isRead, QTeamMemInviteAlarm alarm) {
        if (isRead == null) {
            return null;
        }
        return alarm.isRead.eq(isRead);
    }

    public BooleanExpression searchIsRead(Boolean isRead, QMatchInviteAlarm alarm) {
        if (isRead == null) {
            return null;
        }
        return alarm.isRead.eq(isRead);
    }

    public BooleanExpression searchIsSent(Boolean isSent, Long memberId, QMember fromMember, QMember toMember) {
        if (isSent == null) {
            return fromMember.id.eq(memberId).or(toMember.id.eq(memberId));
        }
        if (isSent) {
            return fromMember.id.eq(memberId);
        }
        return toMember.id.eq(memberId);
    }
}
