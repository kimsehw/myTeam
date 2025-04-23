package com.kimsehw.myteam.service.alarm.impl;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import com.kimsehw.myteam.repository.alarm.AlarmRepository;
import com.kimsehw.myteam.service.alarm.basic.AbstractBasicAlarmServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AlarmServiceImpl extends AbstractBasicAlarmServiceImpl<Alarm> {

    private final AlarmRepository alarmRepository;

    public AlarmServiceImpl(AlarmRepository repository) {
        super(repository);
        alarmRepository = repository;
    }

    @Override
    public Page<Alarm> searchAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable) {
        return alarmRepository.searchAlarms(alarmSearchDto, memberId, pageable);
    }
}
