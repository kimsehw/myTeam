package com.kimsehw.myteam.dto;

import com.kimsehw.myteam.constant.Region;
import com.kimsehw.myteam.constant.TeamRole;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TeamsDto {

    private Long teamMemberId;
    private Long teamId;
    private TeamRole teamRole;
    private String teamName;
    private Region region;
    private LocalDateTime lastMatchDate;
    private LocalDateTime nextMatchDate;

    @QueryProjection
    public TeamsDto(Long teamMemberId, Long teamId, TeamRole teamRole, String teamName, Region region) {
        this.teamMemberId = teamMemberId;
        this.teamId = teamId;
        this.teamRole = teamRole;
        this.teamName = teamName;
        this.region = region;
    }
}
