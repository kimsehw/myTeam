package com.kimsehw.myteam.service.alarm.impl;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.repository.alarm.AlarmRepository;
import com.kimsehw.myteam.service.alarm.basic.AbstractBasicAlarmServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AlarmServiceImpl extends AbstractBasicAlarmServiceImpl<Alarm> {

    public AlarmServiceImpl(AlarmRepository repository) {
        super(repository);
    }
}
