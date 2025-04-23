package com.kimsehw.myteam.service.alarm.factory;

import com.kimsehw.myteam.constant.alarm.AlarmType;
import com.kimsehw.myteam.service.alarm.AlarmService;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AlarmServiceFactory {

    private final Map<String, AlarmService> alarmServiceMap;

    public AlarmServiceFactory(Map<String, AlarmService> alarmServiceMap) {
        this.alarmServiceMap = alarmServiceMap;
    }

    public AlarmService getService(AlarmType alarmType) {
        AlarmService service = alarmServiceMap.get(alarmType.getServiceName());
        if (service == null) {
            throw new IllegalArgumentException("알 수 없는 서비스 타입: " + alarmType);
        }
        return service;
    }
}
