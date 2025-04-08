package com.kimsehw.myteam.dto.match;

import com.kimsehw.myteam.domain.entity.match.Match;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Data;

@Data
public class MatchDto {
    private Long id;
    private String matchDate;
    private String stadium;
    private String result;
    private Integer headCount;
    private TeamInfoDto opposingTeamInfo;
    private List<Long> myTeamIds;

    public MatchDto(Long id, LocalDateTime matchDate, String stadium, String result, Integer headCount,
                    TeamInfoDto opposingTeamInfo) {
        this.id = id;
        this.matchDate = matchDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm"));
        this.stadium = stadium;
        this.result = result;
        this.headCount = headCount;
        this.opposingTeamInfo = opposingTeamInfo;
    }

    public static MatchDto of(Match match) {
        return new MatchDto(match.getId(), match.getMatchDate(), match.getStadium(), match.getResult(),
                match.getHeadCount(), TeamInfoDto.of(match.getOpposingTeam()));
    }

    public static MatchDto ofNotUser(Match match, TeamInfoDto teamInfoDto) {
        return new MatchDto(match.getId(), match.getMatchDate(), match.getStadium(), match.getResult(),
                match.getHeadCount(), teamInfoDto);
    }
}
