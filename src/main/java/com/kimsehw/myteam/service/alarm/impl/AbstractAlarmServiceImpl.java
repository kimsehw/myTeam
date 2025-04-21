package com.kimsehw.myteam.service.alarm.impl;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.repository.alarm.AlarmRepository;
import com.kimsehw.myteam.service.alarm.AlarmService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public abstract class AbstractAlarmServiceImpl<T extends Alarm> implements AlarmService {

    private final AlarmRepository<T> repository;

    @Transactional
    @Override
    public void send(Alarm alarm) {
        repository.save((T) alarm);
    }

    @Transactional
    @Override
    public void read(Long alarmId) {
        Optional<T> alarm = repository.findById(alarmId);
        alarm.ifPresent(Alarm::read);
    }
}
