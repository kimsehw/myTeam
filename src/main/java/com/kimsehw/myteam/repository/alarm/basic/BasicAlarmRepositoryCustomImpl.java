package com.kimsehw.myteam.repository.alarm.basic;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class BasicAlarmRepositoryCustomImpl<T extends Alarm> implements BasicAlarmRepositoryCustom<T> {
    @Override
    public Page<T> searchAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable) {
        return null;
    }
}
