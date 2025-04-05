package com.kimsehw.myteam.domain.entity.team;

import com.kimsehw.myteam.constant.team.AgeRange;
import com.kimsehw.myteam.constant.team.Region;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.TeamMember;
import com.kimsehw.myteam.domain.entity.baseentity.BaseEntity;
import com.kimsehw.myteam.domain.embedded.record.TeamRecord;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Region region;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AgeRange ageRange;

    private String teamDetail;

    @Embedded
    private TeamRecord teamRecord;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> teamMembers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private TeamLogo teamLogo;

    private Team(TeamFormDto teamFormDto, Member member) {
        teamName = teamFormDto.getTeamName();
        region = teamFormDto.getRegion();
        ageRange = teamFormDto.getAgeRange();
        teamDetail = teamFormDto.getTeamDetail();
        this.member = member;
        member.addTeam(this);
        teamRecord = new TeamRecord();
    }

    public static Team createTeam(TeamFormDto teamFormDto, Member member) {
        return new Team(teamFormDto, member);
    }

    public void addTeamMember(TeamMember teamMember) {
        teamMembers.add(teamMember);
    }

    public void updateTeam(TeamInfoDto updateTeamInfoDto) {
        teamName = updateTeamInfoDto.getTeamName();
        region = updateTeamInfoDto.getRegion();
        ageRange = updateTeamInfoDto.getAgeRange();
        teamDetail = updateTeamInfoDto.getTeamDetail();
    }
}
