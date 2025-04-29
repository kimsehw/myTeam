package com.kimsehw.myteam.repository.alarm;

import com.kimsehw.myteam.domain.entity.QMember;
import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.domain.entity.alarm.QAlarm;
import com.kimsehw.myteam.domain.entity.team.QTeam;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import com.kimsehw.myteam.repository.alarm.basic.AbstractBasicAlarmRepositoryCustomImpl;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class AlarmRepositoryCustomImpl extends AbstractBasicAlarmRepositoryCustomImpl<Alarm> implements
        AlarmRepositoryCustom {

    public AlarmRepositoryCustomImpl(EntityManager em) {
        super(em);
    }

    @Override
    public Page<Alarm> searchAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable) {
        QAlarm alarm = QAlarm.alarm;
        QMember fromMember = new QMember("fromMember");
        QMember toMember = new QMember("toMember");
        QTeam fromTeam = new QTeam("fromTeam");
        QTeam toTeam = new QTeam("toTeam");

        Boolean isSent = alarmSearchDto.getIsSent();
        Boolean isRead = alarmSearchDto.getIsRead();
        List<Alarm> alarms = queryFactory.select(alarm)
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
    public Optional<Alarm> findByIdWithFromToMember(Long alarmId) {
        QAlarm alarm = QAlarm.alarm;
        return Optional.ofNullable(queryFactory.select(alarm).from(alarm)
                .join(alarm.fromMember).fetchJoin()
                .join(alarm.toMember).fetchJoin()
                .where(alarm.id.eq(alarmId))
                .fetchOne());
    }
}
