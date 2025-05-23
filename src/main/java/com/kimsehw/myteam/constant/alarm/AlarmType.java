package com.kimsehw.myteam.constant.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
    ALARM("전체", "alarmServiceImpl"), TEAM_INVITE("팀 초대", "teamMemInviteAlarmServiceImpl"),
    MATCH_INVITE("매치 초대", "matchInviteAlarmServiceImpl"),
    TEAM_INVITE_RESPONSE("팀 초대 응답", "teamMemInviteAlarmServiceImpl"),
    MATCH_RESPONSE("매치 초대 응답", "matchInviteAlarmServiceImpl");

    private String typeName;
    private String serviceName;

    public boolean isTypeOfInvite() {
        return this.equals(TEAM_INVITE) || this.equals(MATCH_INVITE)
                || this.equals(TEAM_INVITE_RESPONSE) || this.equals(MATCH_RESPONSE);
    }
}
