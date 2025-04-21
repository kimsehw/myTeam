package com.kimsehw.myteam.domain.entity.alarm;

import com.kimsehw.myteam.constant.alarm.AlarmType;
import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.dto.match.MatchInviteFormDto;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    private Long fromTeamId;

    private int playerNum;

    private String matchDate;

    private int matchTime;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_mem_id")
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_mem_id")
    private Member toMember;

    private Alarm(Member fromMember, Member toMember, Long fromTeamId, AlarmType alarmType, int playerNum) {
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.fromTeamId = fromTeamId;
        this.alarmType = alarmType;
        this.playerNum = playerNum;
    }

    public Alarm(Member fromMember, Member toMember, Long fromTeamId, String matchDate, int matchTime,
                 AlarmType alarmType) {
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.fromTeamId = fromTeamId;
        this.matchDate = matchDate;
        this.matchTime = matchTime;
        this.alarmType = alarmType;
    }

    public static Alarm createTeamMemInviteAlarm(Member fromMember, Member toMember, Long fromTeamId, int playerNum) {
        return new Alarm(fromMember, toMember, fromTeamId, AlarmType.TEAM_MEM_INVITE, playerNum);
    }

    public static Alarm createMatchInviteAlarm(Member fromMember, Member toMember, Long fromTeamId,
                                               MatchInviteFormDto matchInviteFormDto) {
        return new Alarm(fromMember, toMember, fromTeamId, matchInviteFormDto.getMatchDate(),
                matchInviteFormDto.getMatchTime(), AlarmType.TEAM_MATCH_REQUEST);
    }
}
