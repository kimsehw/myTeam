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

    private String alarmType;

    private boolean isRead;

    private String detail;

    public AlarmDto(Long id, Long fromMemId, Long toMemId, Long fromTeamId, Long toTeamId, String summary,
                    AlarmType alarmType, boolean isRead, String detail) {
        this.id = id;
        this.fromMemId = fromMemId;
        this.toMemId = toMemId;
        this.fromTeamId = fromTeamId;
        this.toTeamId = toTeamId;
        this.summary = summary;
        this.alarmType = alarmType.getTypeName();
        this.isRead = isRead;
        this.detail = detail;
    }

    public static AlarmDto ofSent(Alarm alarm) {
        return new AlarmDto(alarm.getId(), alarm.getFromMember().getId(), alarm.getToMember().getId(),
                alarm.getFromTeam().getId(), alarm.getToTeam() != null ? alarm.getToTeam().getId() : null,
                alarm.getSummary(true), alarm.getType(),
                alarm.isRead(), alarm.getDetailMessage(true));
    }

    public static AlarmDto ofReceive(Alarm alarm) {
        return new AlarmDto(alarm.getId(), alarm.getFromMember().getId(), alarm.getToMember().getId(),
                alarm.getFromTeam().getId(), alarm.getToTeam() != null ? alarm.getToTeam().getId() : null,
                alarm.getSummary(false), alarm.getType(),
                alarm.isRead(), alarm.getDetailMessage(false));
    }
}
