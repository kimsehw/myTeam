package com.kimsehw.myteam.repository.alarm.basic;

import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BasicAlarmRepositoryCustom<T> {
    Page<T> searchAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable);

    Optional<T> findByIdWithFromToMember(Long alarmId);
}
