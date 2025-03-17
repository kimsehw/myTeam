package com.kimsehw.myteam.dto.team;

import com.kimsehw.myteam.constant.AgeRange;
import com.kimsehw.myteam.constant.Region;
import com.kimsehw.myteam.dto.ScheduleDto;
import com.kimsehw.myteam.embedded.record.TeamRecord;
import com.kimsehw.myteam.entity.team.Team;
import java.util.List;
import lombok.Data;

@Data
public class TeamInfoDto {

    private Long teamId;
    private String teamName;
    private String logoUrl;
    private int memberNum;
    private Region region;
    private AgeRange ageRange;
    private int wins;
    private int loses;
    private int draws;
    private String winRate;
    private String teamDetail;
    private List<ScheduleDto> schedules;

    public TeamInfoDto(Long teamId, String teamName, String logoUrl, int memberNum, Region region, AgeRange ageRange,
                       TeamRecord teamRecord, String teamDetail) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.logoUrl = logoUrl;
        this.memberNum = memberNum;
        this.region = region;
        this.ageRange = ageRange;
        this.wins = teamRecord.getWins();
        this.loses = teamRecord.getLoses();
        this.draws = teamRecord.getDraws();
        this.winRate = teamRecord.getWinRate();
        this.teamDetail = teamDetail;
    }

    public static TeamInfoDto of(Team team) {
        return new TeamInfoDto(team.getId(), team.getTeamName(), team.getTeamLogo().getImgUrl(),
                team.getTeamMembers().size(), team.getRegion(), team.getAgeRange(),
                team.getTeamRecord(), team.getTeamDetail());
    }
}

