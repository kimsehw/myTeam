package com.kimsehw.myteam.dto.match;

import com.kimsehw.myteam.constant.team.AgeRange;
import com.kimsehw.myteam.constant.team.Region;
import lombok.Data;

@Data
public class MatchUpdateFormDto {
    private Long id;
    private String stadium;
    private String matchDate;
    private Integer matchTime;
    private Boolean isNotUserMatch;
    private String opposingTeamName;
    private Region opposingTeamRegion;
    private AgeRange opposingTeamAgeRange;
}
