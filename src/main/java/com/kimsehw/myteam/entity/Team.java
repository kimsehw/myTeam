package com.kimsehw.myteam.entity;

import com.kimsehw.myteam.constant.AgeRange;
import com.kimsehw.myteam.constant.Region;
import com.kimsehw.myteam.dto.TeamFormDto;
import jakarta.persistence.Column;
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
@Table(name = "team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String teamName;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Enumerated(EnumType.STRING)
    private AgeRange ageRange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private Team(TeamFormDto teamFormDto, Member member) {
        teamName = teamFormDto.getTeamName();
        region = teamFormDto.getRegion();
        ageRange = teamFormDto.getAgeRange();
        this.member = member;
        member.addTeam(this);
    }

    public static Team createTeam(TeamFormDto teamFormDto, Member member) {
        return new Team(teamFormDto, member);
    }
}
