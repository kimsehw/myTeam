package com.kimsehw.myteam.dto.alarm;

import com.kimsehw.myteam.constant.alarm.AlarmType;
import lombok.Data;

@Data
public class AlarmSearchDto {

    private Boolean isRead;

    private Boolean isSent;

    private AlarmType alarmType;
}
