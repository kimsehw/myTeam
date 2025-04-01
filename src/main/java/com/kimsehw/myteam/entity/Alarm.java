package com.kimsehw.myteam.entity;

import com.kimsehw.myteam.constant.alarm.AlarmType;
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
import java.time.LocalDateTime;
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

    private LocalDateTime matchDate;

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

    public static Alarm createInviteAlarm(Member fromMember, Member toMember, Long fromTeamId, int playerNum) {
        return new Alarm(fromMember, toMember, fromTeamId, AlarmType.TEAM_MEM_INVITE, playerNum);
    }
}
