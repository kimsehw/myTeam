package com.kimsehw.myteam.service.alarm;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;

public interface AlarmService {
    void send(Alarm alarm);

    void read(Long alarmId);
}
