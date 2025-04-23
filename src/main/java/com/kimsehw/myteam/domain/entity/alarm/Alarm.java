package com.kimsehw.myteam.domain.entity.alarm;

import com.kimsehw.myteam.constant.alarm.AlarmType;
import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.baseentity.BaseTimeEntity;
import com.kimsehw.myteam.domain.entity.team.Team;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "alarm_type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Alarm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_mem_id")
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_mem_id")
    private Member toMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromTeam_id")
    private Team fromTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toTeam_id")
    private Team toTeam;

    @Column(name = "alarm_type", insertable = false, updatable = false)
    private String alarmType;

    public Alarm(Member fromMember, Member toMember, Team fromTeam) {
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.fromTeam = fromTeam;
    }

    public Alarm(Member fromMember, Member toMember, Team fromTeam, Team toTeam) {
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.fromTeam = fromTeam;
        this.toTeam = toTeam;
    }


    public abstract String getSummary(boolean isSent);

    public abstract String getDetailMessage(boolean isSent);

    public void read() {
        isRead = true;
    }

    public abstract AlarmType getType();
}
