package com.kimsehw.myteam.dto.teammember;

import com.kimsehw.myteam.constant.Position;
import com.kimsehw.myteam.constant.TeamRole;
import com.kimsehw.myteam.embedded.record.PersonalRecord;
import com.kimsehw.myteam.entity.TeamMember;
import lombok.Data;

@Data
public class TeamMemberDto {

    private Long teamMemId;
    private TeamRole teamRole;
    private int playerNum;
    private String name;
    private Position position;
    private int goals;
    private int assist;
    private int attendance;

    public TeamMemberDto(Long teamMemId, TeamRole teamRole, int playerNum, String name,
                         PersonalRecord record, int attendance) {
        this.teamMemId = teamMemId;
        this.teamRole = teamRole;
        this.playerNum = playerNum;
        this.name = name;
        this.position = record.getPosition();
        this.goals = record.getGoals();
        this.assist = record.getAssist();
        this.attendance = attendance;
    }

    public static TeamMemberDto of(TeamMember teamMember) {
        return new TeamMemberDto(teamMember.getId(), teamMember.getTeamRole(), teamMember.getPlayerNum(),
                teamMember.getName(), teamMember.getTeamMemberRecord(), teamMember.getAttendance());
    }
}
