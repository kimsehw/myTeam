package com.kimsehw.myteam.repository.alarm.basic;

import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BasicAlarmRepositoryCustom<T> {
    Page<T> searchAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable);
}
