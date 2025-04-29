package com.kimsehw.myteam.repository.alarm.invite.match;

import com.kimsehw.myteam.constant.alarm.AlarmType;
import com.kimsehw.myteam.domain.entity.QMember;
import com.kimsehw.myteam.domain.entity.alarm.MatchInviteAlarm;
import com.kimsehw.myteam.domain.entity.alarm.QMatchInviteAlarm;
import com.kimsehw.myteam.domain.entity.team.QTeam;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import com.kimsehw.myteam.repository.alarm.basic.AbstractBasicAlarmRepositoryCustomImpl;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class MatchInviteAlarmRepositoryCustomImpl extends
        AbstractBasicAlarmRepositoryCustomImpl<MatchInviteAlarm> implements MatchInviteAlarmRepositoryCustom {

    public MatchInviteAlarmRepositoryCustomImpl(EntityManager em) {
        super(em);
    }

    @Override
    public Page<MatchInviteAlarm> searchAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable) {
        QMatchInviteAlarm alarm = QMatchInviteAlarm.matchInviteAlarm;
        QMember fromMember = new QMember("fromMember");
        QMember toMember = new QMember("toMember");
        QTeam fromTeam = new QTeam("fromTeam");
        QTeam toTeam = new QTeam("toTeam");

        Boolean isSent = alarmSearchDto.getIsSent();
        Boolean isRead = alarmSearchDto.getIsRead();
        List<MatchInviteAlarm> alarms = queryFactory.select(alarm)
                .from(alarm)
                .join(alarm.fromMember, fromMember).fetchJoin()
                .join(alarm.toMember, toMember).fetchJoin()
                .leftJoin(alarm.fromTeam, fromTeam).fetchJoin()
                .leftJoin(alarm.toTeam, toTeam).fetchJoin()
                .where(searchIsRead(isRead, alarm), searchIsSent(isSent, memberId, fromMember, toMember),
                        exceptHide(memberId, fromMember, toMember, alarm))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(alarm.count())
                .from(alarm)
                .where(searchIsRead(isRead, alarm), searchIsSent(isSent, memberId, fromMember, toMember))
                .fetchOne();

        return new PageImpl<>(alarms, pageable, total == null ? 0 : total);
    }

    @Override
    public Optional<MatchInviteAlarm> findByIdWithFromToMember(Long alarmId) {
        QMatchInviteAlarm alarm = QMatchInviteAlarm.matchInviteAlarm;
        return Optional.ofNullable(queryFactory.select(alarm).from(alarm)
                .join(alarm.fromMember).fetchJoin()
                .join(alarm.toMember).fetchJoin()
                .where(alarm.id.eq(alarmId))
                .fetchOne());
    }

    @Override
    public List<Long> findToTeamIdsByFromMemberIdAndFromTeamId(Long fromMemId, Long teamId) {
        QMatchInviteAlarm alarm = QMatchInviteAlarm.matchInviteAlarm;
        return queryFactory.select(alarm.toTeam.id).from(alarm)
                .where(alarm.fromMember.id.eq(fromMemId)
                        .and(alarm.fromTeam.id.eq(teamId))
                        .and(alarm.alarmType.eq(AlarmType.MATCH_INVITE)))
                .fetch();
    }
}
