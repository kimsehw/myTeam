package com.kimsehw.myteam.dto.member;

import lombok.Data;

@Data
public class MyTeamsInfoDto {

    private Long teamId;
    private String teamName;

    public MyTeamsInfoDto(Long teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }
}
