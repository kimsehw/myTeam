package com.kimsehw.myteam.service.alarm.basic;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.dto.alarm.AlarmDto;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import com.kimsehw.myteam.repository.alarm.basic.BasicAlarmRepository;
import com.kimsehw.myteam.service.alarm.AlarmService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public abstract class AbstractBasicAlarmServiceImpl<T extends Alarm> implements AlarmService<T> {

    private final BasicAlarmRepository<T> repository;

    @Transactional
    @Override
    public void send(T alarm) {
        repository.save(alarm);
    }

    @Transactional
    @Override
    public void read(Long alarmId) {
        Optional<T> alarm = repository.findById(alarmId);
        alarm.ifPresent(Alarm::read);
    }

    @Override
    public Page<AlarmDto> getMyAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable) {
        return searchAlarms(alarmSearchDto, memberId, pageable).map(AlarmDto::of);
    }

    public abstract Page<T> searchAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable);
}
