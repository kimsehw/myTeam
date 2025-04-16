package com.kimsehw.myteam.dto.teammember;

import lombok.Data;

@Data
public class TeamMemberForAddMatchDto {
    private Long teamMemId;
    private int playerNum;
    private String name;
    private Boolean isAlreadyIn;

    public TeamMemberForAddMatchDto(Long teamMemId, int playerNum, String name, Boolean isAlreadyIn) {
        this.teamMemId = teamMemId;
        this.playerNum = playerNum;
        this.name = name;
        this.isAlreadyIn = isAlreadyIn;
    }
}
