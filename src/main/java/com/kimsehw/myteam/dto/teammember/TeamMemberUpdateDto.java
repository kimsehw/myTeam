package com.kimsehw.myteam.dto.teammember;

import com.kimsehw.myteam.constant.Position;
import com.kimsehw.myteam.constant.TeamRole;
import lombok.Data;

@Data
public class TeamMemberUpdateDto {

    private Long teamMemId;
    private TeamRole teamRole;
    private String name;
    private Integer playerNum;
    private Position position;
}
