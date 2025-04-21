package com.kimsehw.myteam.constant.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
    TEAM_MEM_INVITE("team_invite"), TEAM_MATCH_REQUEST("match_invite"), RESPONSE("response");

    private String typeName;
}
