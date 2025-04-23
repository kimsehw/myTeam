package com.kimsehw.myteam.repository.alarm.invite.team;

import com.kimsehw.myteam.domain.entity.QMember;
import com.kimsehw.myteam.domain.entity.alarm.QTeamMemInviteAlarm;
import com.kimsehw.myteam.domain.entity.alarm.TeamMemInviteAlarm;
import com.kimsehw.myteam.domain.entity.team.QTeam;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import com.kimsehw.myteam.repository.alarm.basic.AbstractBasicAlarmRepositoryCustomImpl;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class TeamMemInviteAlarmRepositoryCustomImpl extends
        AbstractBasicAlarmRepositoryCustomImpl<TeamMemInviteAlarm> implements TeamMemInviteAlarmRepositoryCustom {

    public TeamMemInviteAlarmRepositoryCustomImpl(EntityManager em) {
        super(em);
    }

    @Override
    public Page<TeamMemInviteAlarm> searchAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable) {
        QTeamMemInviteAlarm alarm = QTeamMemInviteAlarm.teamMemInviteAlarm;
        QMember fromMember = new QMember("fromMember");
        QMember toMember = new QMember("toMember");
        QTeam fromTeam = new QTeam("fromTeam");
        QTeam toTeam = new QTeam("toTeam");

        Boolean isSent = alarmSearchDto.getIsSent();
        Boolean isRead = alarmSearchDto.getIsRead();
        List<TeamMemInviteAlarm> alarms = queryFactory.select(alarm)
                .from(alarm)
                .join(alarm.fromMember, fromMember).fetchJoin()
                .join(alarm.toMember, toMember).fetchJoin()
                .leftJoin(alarm.fromTeam, fromTeam).fetchJoin()
                .where(searchIsRead(isRead, alarm), searchIsSent(isSent, memberId, fromMember, toMember))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory.select(alarm.count())
                .from(alarm)
                .where(searchIsRead(isRead, alarm), searchIsSent(isSent, memberId, fromMember, toMember))
                .fetchOne();

        return new PageImpl<>(alarms, pageable, total == null ? 0 : total);
    }
}
