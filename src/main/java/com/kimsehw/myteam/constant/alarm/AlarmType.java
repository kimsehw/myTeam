package com.kimsehw.myteam.constant.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
    TEAM_INVITE("팀 초대"), MATCH_INVITE("매치 초대"), TEAM_INVITE_RESPONSE("팀 초대 응답"), MATCH_RESPONSE("매치 초대 응답");

    private String typeName;
}
