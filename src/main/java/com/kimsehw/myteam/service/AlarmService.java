package com.kimsehw.myteam.service;

import com.kimsehw.myteam.entity.Alarm;
import com.kimsehw.myteam.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public Long save(Alarm alarm) {
        Alarm save = alarmRepository.save(alarm);
        return save.getId();
    }
}
