package com.kimsehw.myteam.domain.entity.team;

import com.kimsehw.myteam.constant.team.AgeRange;
import com.kimsehw.myteam.constant.team.Region;
import com.kimsehw.myteam.domain.embedded.record.TeamRecord;
import com.kimsehw.myteam.domain.entity.TeamMember;
import com.kimsehw.myteam.domain.entity.baseentity.BaseEntity;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
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
    @JoinColumn(name = "leader_id")
    private TeamMember leader;

    @Setter
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "logo_id")
    private TeamLogo teamLogo;

    private Team(TeamFormDto teamFormDto, TeamMember leader) {
        teamName = teamFormDto.getTeamName();
        region = teamFormDto.getRegion();
        ageRange = teamFormDto.getAgeRange();
        teamDetail = teamFormDto.getTeamDetail();
        this.leader = leader;
        teamRecord = new TeamRecord();
        addTeamMember(leader);
        leader.beLeaderOf(this);
    }

    public static Team createTeam(TeamFormDto teamFormDto, TeamMember leader) {
        return new Team(teamFormDto, leader);
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

    public boolean isTeamMember(Long id) {
        for (TeamMember teamMember : teamMembers) {
            if (teamMember.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean areTheyTeamMember(List<Long> teamMemIds) {
        for (Long teamMemId : teamMemIds) {
            if (!isTeamMember(teamMemId)) {
                return false;
            }
        }
        return true;
    }
}
