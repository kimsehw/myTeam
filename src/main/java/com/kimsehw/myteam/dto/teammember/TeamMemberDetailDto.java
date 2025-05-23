package com.kimsehw.myteam.dto.teammember;

import com.kimsehw.myteam.constant.Position;
import com.kimsehw.myteam.constant.teammember.TeamRole;
import com.kimsehw.myteam.domain.entity.TeamMember;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamMemberDetailDto {

    private Long id;
    private String name;
    private String detail;
    private int playerNum;
    private TeamRole teamRole;
    private int attendance;
    private int goals;
    private int assist;
    private int wins;
    private int loses;
    private int draws;
    private String winRate;
    private Position position;
    /*매치 관련 추가 예정*/

    private TeamMemberDetailDto(TeamMember teamMember) {
        this.id = teamMember.getId();
        this.name = teamMember.getName();
        this.detail = teamMember.getDetail();
        this.playerNum = teamMember.getPlayerNum();
        this.teamRole = teamMember.getTeamRole();
        this.attendance = teamMember.getAttendance();
        this.goals = teamMember.getTeamMemberRecord().getGoals();
        this.assist = teamMember.getTeamMemberRecord().getAssist();
        this.wins = teamMember.getTeamMemberRecord().getWins();
        this.loses = teamMember.getTeamMemberRecord().getLoses();
        this.draws = teamMember.getTeamMemberRecord().getDraws();
        this.winRate = teamMember.getTeamMemberRecord().getWinRate();
        this.position = teamMember.getTeamMemberRecord().getPosition();
    }

    public static TeamMemberDetailDto of(TeamMember teamMember) {
        return new TeamMemberDetailDto(teamMember);
    }
}
