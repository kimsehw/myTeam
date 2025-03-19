package com.kimsehw.myteam.entity;

import com.kimsehw.myteam.constant.TeamRole;
import com.kimsehw.myteam.embedded.record.PersonalRecord;
import com.kimsehw.myteam.entity.baseentity.BaseEntity;
import com.kimsehw.myteam.entity.team.Team;
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

    private TeamMember(Team team, Member member, TeamRole teamRole) {
        this.team = team;
        this.member = member;
        this.teamRole = teamRole;
        name = member.getName();
        teamMemberRecord = new PersonalRecord(member.getMemberRecord().getPosition());
        team.addTeamMember(this);
    }

    public static TeamMember createTeamMember(Team team, Member member, TeamRole teamRole) {
        return new TeamMember(team, member, teamRole);
    }
}
