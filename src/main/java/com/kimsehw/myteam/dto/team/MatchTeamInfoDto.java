package com.kimsehw.myteam.dto.team;

import com.kimsehw.myteam.constant.team.AgeRange;
import com.kimsehw.myteam.constant.team.Region;
import com.kimsehw.myteam.domain.entity.TeamMember;
import com.kimsehw.myteam.domain.entity.team.Team;
import lombok.Data;

@Data
public class MatchTeamInfoDto {

    private Long teamId;
    private String teamName;
    private String leaderEmail;
    private String leaderName;
    private String region;
    private String ageRange;

    public MatchTeamInfoDto(Long teamId, String teamName, String leaderEmail, String leaderName, Region region,
                            AgeRange ageRange) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.leaderEmail = leaderEmail;
        this.leaderName = leaderName;
        this.region = region.getRegionName();
        this.ageRange = ageRange.getRange();
    }

    public static MatchTeamInfoDto of(Team team) {
        TeamMember leader = team.getLeader();
        return new MatchTeamInfoDto(team.getId(), team.getTeamName(), leader.getMember().getEmail(), leader.getName(),
                team.getRegion(), team.getAgeRange());
    }
}
