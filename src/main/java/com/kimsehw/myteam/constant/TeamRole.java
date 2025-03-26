package com.kimsehw.myteam.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeamRole {
    MEMBER("선 수"), LEADER("회 장"), SUB_LEADER("부회장"), SECRETARY("총 무");

    private String roleName;

    public boolean isAuthorizedToManageTeam() {
        return (this == LEADER || this == SUB_LEADER);
    }
}
