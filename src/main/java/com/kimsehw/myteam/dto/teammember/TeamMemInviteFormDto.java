package com.kimsehw.myteam.dto.teammember;

import lombok.Data;

@Data
public class TeamMemInviteFormDto {

    private String email;
    private boolean isUser;
    private Integer playerNum;

}
