package com.kimsehw.myteam.service.alarm.factory;

import com.kimsehw.myteam.constant.alarm.AlarmType;
import com.kimsehw.myteam.service.alarm.AlarmService;
import com.kimsehw.myteam.service.alarm.invite.InviteAlarmService;
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

    public InviteAlarmService getInviteService(AlarmType alarmType) {
        if (!isTypeOfInvite(alarmType)) {
            throw new IllegalArgumentException("Invite Service 타입이 아닙니다: " + alarmType);
        }
        return (InviteAlarmService) alarmServiceMap.get(alarmType.getServiceName());
    }

    private static boolean isTypeOfInvite(AlarmType alarmType) {
        return alarmType.isTypeOfInvite();
    }
}
