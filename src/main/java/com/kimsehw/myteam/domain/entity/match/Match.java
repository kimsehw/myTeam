package com.kimsehw.myteam.domain.entity.match;

import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.domain.utill.DateTimeUtil;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "match_games")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    private LocalDateTime matchDate;

    private Integer matchTime;

    private String stadium;
    private String notUserOpposingTeamName;

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

    public static Match createMatchOf(Team myTeam, String inviteeTeamName, String matchDate, Integer matchTime) {
        return new Match(myTeam, inviteeTeamName, matchDate, matchTime);
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
}
