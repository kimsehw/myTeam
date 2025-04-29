package com.kimsehw.myteam.dto.alarm;

import com.kimsehw.myteam.constant.alarm.AlarmType;
import lombok.Data;

@Data
public class AlarmResponseFormDto {
    private Long inviteAlarmId;
    private Long fromMemId;
    private Long toMemId;
    private Long fromTeamId;
    private Long toTeamId;

    private AlarmType alarmType;

    private Boolean response;
}
