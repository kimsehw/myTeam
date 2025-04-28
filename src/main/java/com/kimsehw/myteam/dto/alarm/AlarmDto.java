package com.kimsehw.myteam.dto.alarm;

import com.kimsehw.myteam.constant.alarm.AlarmType;
import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.domain.entity.team.Team;
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

    public static AlarmDto from(Alarm alarm, Long memberId) {
        boolean isSent = alarm.isFrom(memberId);
        return new AlarmDto(alarm.getId(), alarm.getFromMember().getId(), alarm.getToMember().getId(),
                getTeamIdOf(alarm.getFromTeam()), getTeamIdOf(alarm.getToTeam()), alarm.getSummary(isSent),
                alarm.getAlarmType(), alarm.isRead(), isSent, alarm.getDetailMessage(isSent));
    }

    private static Long getTeamIdOf(Team fromOrToTeam) {
        if (fromOrToTeam != null) {
            return fromOrToTeam.getId();
        }
        return null;
    }
}
