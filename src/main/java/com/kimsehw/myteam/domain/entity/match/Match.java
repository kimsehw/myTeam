package com.kimsehw.myteam.domain.entity.match;

import com.kimsehw.myteam.constant.team.AgeRange;
import com.kimsehw.myteam.constant.team.Region;
import com.kimsehw.myteam.domain.entity.TeamMember;
import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.domain.utill.DateTimeUtil;
import com.kimsehw.myteam.dto.match.MatchUpdateFormDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "match_games")
@Log
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    private LocalDateTime matchDate;

    private Integer matchTime;

    private String stadium;

    private String notUserOpposingTeamName;
    private Region notUserOpposingTeamRegion;
    private AgeRange notUserOpposingTeamAgeRange;

    private Boolean isDone;

    private Integer goal;
    private Integer lostGoal;

    private Integer headCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "myTeam_id")
    private Team myTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opposingTeam_id")
    private Team opposingTeam;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    List<MatchMemberRecord> matchMemberRecords;

    public Match(Team myTeam, String inviteeTeamName, String matchDate, Integer matchTime) {
        this.myTeam = myTeam;
        this.notUserOpposingTeamName = inviteeTeamName;
        this.matchDate = DateTimeUtil.formatting(matchDate, DateTimeUtil.Y_M_D_H_M_TYPE);
        this.matchTime = matchTime;
        this.isDone = false;
    }

    public Match(Team myTeam, Team opposingTeam, LocalDateTime matchDate, Integer matchTime) {
        this.myTeam = myTeam;
        this.opposingTeam = opposingTeam;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.isDone = false;
    }

    public static Match createNotUserMatchOf(Team myTeam, String inviteeTeamName, String matchDate, Integer matchTime) {
        return new Match(myTeam, inviteeTeamName, matchDate, matchTime);
    }

    public static Match createMatchOf(Team myTeam, Team opposingTeam, LocalDateTime matchDate, Integer matchTime) {
        return new Match(myTeam, opposingTeam, matchDate, matchTime);
    }

    public String getResult() {
        if (!isDone) {
            return "결과 없음(경기 전)";
        }
        if (goal > lostGoal) {
            return String.format("%d : %d 승리", goal, lostGoal);
        }
        if (goal.equals(lostGoal)) {
            return String.format("%d : %d 무승부", goal, lostGoal);
        }
        return String.format("%d : %d 패배", goal, lostGoal);
    }

    public void addMembers(List<TeamMember> addMembers) {
        Map<Long, MatchMemberRecord> teamMemIdAndRecord = getTeamMemIdAndRecord();
        for (TeamMember addMember : addMembers) {
            Long addMemberId = addMember.getId();
            if (!isAlreadyIn(teamMemIdAndRecord, addMemberId)) {
                MatchMemberRecord record = MatchMemberRecord.createRecordOf(this, addMember);
                matchMemberRecords.add(record);
                continue;
            }
            teamMemIdAndRecord.remove(addMemberId);
        }
        matchMemberRecords.removeIf(teamMemIdAndRecord::containsValue);
        headCount = matchMemberRecords.size();
    }

    private Map<Long, MatchMemberRecord> getTeamMemIdAndRecord() {
        return matchMemberRecords.stream()
                .collect(Collectors.toMap(matchMemberRecord -> matchMemberRecord.getTeamMember().getId(),
                        matchMemberRecord -> matchMemberRecord));
    }

    public boolean isAlreadyIn(Map<Long, MatchMemberRecord> teamMemIdAndRecord, Long teamMemId) {
        return teamMemIdAndRecord.containsKey(teamMemId);
    }

    public boolean isAlreadyIn(Long teamMemId) {
        Map<Long, MatchMemberRecord> teamMemIdAndRecord = getTeamMemIdAndRecord();
        return teamMemIdAndRecord.containsKey(teamMemId);
    }

    public void update(MatchUpdateFormDto matchUpdateFormDto) {
        matchTime = matchUpdateFormDto.getMatchTime();
        matchDate = DateTimeUtil.formatting(matchUpdateFormDto.getMatchDate(), DateTimeUtil.Y_M_D_H_M_TYPE);
        stadium = matchUpdateFormDto.getStadium();
        if (matchUpdateFormDto.getIsNotUserMatch()) {
            updateNotUserInfo(matchUpdateFormDto);
        }
    }

    private void updateNotUserInfo(MatchUpdateFormDto matchUpdateFormDto) {
        notUserOpposingTeamName = matchUpdateFormDto.getOpposingTeamName();
        notUserOpposingTeamRegion = matchUpdateFormDto.getOpposingTeamRegion();
        notUserOpposingTeamAgeRange = matchUpdateFormDto.getOpposingTeamAgeRange();
    }
}
