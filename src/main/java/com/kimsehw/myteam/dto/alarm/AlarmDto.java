package com.kimsehw.myteam.dto.alarm;

import com.kimsehw.myteam.constant.alarm.AlarmType;
import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import lombok.Data;

@Data
public class AlarmDto {

    private Long id;
    private Long fromMemId;
    private Long toMemId;
    private Long fromTeamId;
    private Long toTeamId;

    private String summary;

    private AlarmType alarmType;
    private String alarmTypeName;

    private boolean isRead;
    private boolean isSent;

    private String detail;

    public AlarmDto(Long id, Long fromMemId, Long toMemId, Long fromTeamId, Long toTeamId, String summary,
                    AlarmType alarmType, boolean isRead, boolean isSent, String detail) {
        this.id = id;
        this.fromMemId = fromMemId;
        this.toMemId = toMemId;
        this.fromTeamId = fromTeamId;
        this.toTeamId = toTeamId;
        this.summary = summary;
        this.alarmType = alarmType;
        this.alarmTypeName = alarmType.getTypeName();
        this.isRead = isRead;
        this.isSent = isSent;
        this.detail = detail;
    }

    public static AlarmDto ofSent(Alarm alarm) {
        boolean isSent = true;
        return new AlarmDto(alarm.getId(), alarm.getFromMember().getId(), alarm.getToMember().getId(),
                alarm.getFromTeam().getId(), alarm.getToTeam() != null ? alarm.getToTeam().getId() : null,
                alarm.getSummary(isSent), alarm.getAlarmType(),
                alarm.isRead(), isSent, alarm.getDetailMessage(isSent));
    }

    public static AlarmDto ofReceive(Alarm alarm) {
        boolean isSent = false;
        return new AlarmDto(alarm.getId(), alarm.getFromMember().getId(), alarm.getToMember().getId(),
                alarm.getFromTeam().getId(), alarm.getToTeam() != null ? alarm.getToTeam().getId() : null,
                alarm.getSummary(isSent), alarm.getAlarmType(),
                alarm.isRead(), isSent, alarm.getDetailMessage(isSent));
    }
}
