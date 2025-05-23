package com.kimsehw.myteam.domain.entity;

import com.kimsehw.myteam.constant.Position;
import com.kimsehw.myteam.constant.teammember.TeamRole;
import com.kimsehw.myteam.domain.embedded.record.PersonalRecord;
import com.kimsehw.myteam.domain.entity.baseentity.BaseEntity;
import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.dto.teammember.TeamMemberUpdateDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "team_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teamMember_id")
    private Long id;

    private String name;
    private String detail;
    private int attendance;

    //나중에
    //@Column(unique = true)
    private int playerNum;

    @Enumerated(EnumType.STRING)
    private TeamRole teamRole;

    @Embedded
    private PersonalRecord teamMemberRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private TeamMember(Member member, TeamRole teamRole) {
        this.member = member;
        this.teamRole = teamRole;
        name = member.getName();
        teamMemberRecord = new PersonalRecord(member.getMemberRecord().getPosition());
        member.addMyTeam(this);
    }

    public TeamMember(Team team, Member member, TeamRole teamRole, int playerNum) {
        this.team = team;
        this.member = member;
        this.teamRole = teamRole;
        this.playerNum = playerNum;
        name = member.getName();
        teamMemberRecord = new PersonalRecord(member.getMemberRecord().getPosition());
        team.addTeamMember(this);
    }

    public TeamMember(Team team, String name, TeamRole teamRole, Integer playerNum, Position position) {
        this.team = team;
        this.teamRole = teamRole;
        this.playerNum = playerNum;
        this.name = name;
        teamMemberRecord = new PersonalRecord(position);
        team.addTeamMember(this);
    }

    /**
     * 팀 생성 시 리더로 추가합니다.
     *
     * @param member 리더 회원
     * @return TeamMember leader
     */
    public static TeamMember createInitialTeamMember(Member member) {
        return new TeamMember(member, TeamRole.LEADER);
    }

    public static TeamMember createTeamMember(Team team, Member member, TeamRole teamRole, int playerNum) {
        return new TeamMember(team, member, teamRole, playerNum);
    }

    public static TeamMember createNotUserTeamMember(Team team, String name, TeamRole teamRole, Integer playerNum,
                                                     Position position) {
        return new TeamMember(team, name, teamRole, playerNum, position);
    }

    public void updateBy(TeamMemberUpdateDto teamMemberUpdateDto) {
        this.teamRole = teamMemberUpdateDto.getTeamRole();
        this.name = teamMemberUpdateDto.getName();
        this.playerNum = teamMemberUpdateDto.getPlayerNum();
        this.teamMemberRecord.changePosition(teamMemberUpdateDto.getPosition());
    }

    public void beLeaderOf(Team team) {
        this.team = team;
    }
}
