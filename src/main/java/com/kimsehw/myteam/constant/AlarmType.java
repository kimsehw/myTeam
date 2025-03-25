package com.kimsehw.myteam.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
    TEAM_MEM_INVITE, TEAM_MEM_INVITE_RESPONSE, TEAM_MATCH_REQUEST, TEAM_MATCH_RESPONSE;
}
