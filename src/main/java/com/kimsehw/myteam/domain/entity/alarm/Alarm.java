package com.kimsehw.myteam.domain.entity.alarm;

import com.kimsehw.myteam.constant.alarm.AlarmType;
import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.baseentity.BaseTimeEntity;
import com.kimsehw.myteam.domain.entity.team.Team;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(name = "detail_type")
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    public Alarm(Member fromMember, Member toMember, Team fromOrToTeam, AlarmType alarmType) {
        this.fromMember = fromMember;
        this.toMember = toMember;
        injectAboutTeamInvite(fromOrToTeam, alarmType);
        this.alarmType = alarmType;
    }

    private void injectAboutTeamInvite(Team fromOrToTeam, AlarmType alarmType) {
        if (alarmType.equals(AlarmType.TEAM_INVITE)) {
            this.fromTeam = fromOrToTeam;
        }
        if (alarmType.equals(AlarmType.TEAM_INVITE_RESPONSE)) {
            this.toTeam = fromOrToTeam;
        }
    }

    public Alarm(Member fromMember, Member toMember, Team fromTeam, Team toTeam, AlarmType alarmType) {
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.fromTeam = fromTeam;
        this.toTeam = toTeam;
        this.alarmType = alarmType;
    }


    public abstract String getSummary(boolean isSent);

    public abstract String getDetailMessage(boolean isSent);

    public void read() {
        isRead = true;
    }

    /**
     * 해당 유저가 보낸 메일인지 검사합니다.
     *
     * @param email 해당 유저의 아이디
     * @return boolean
     */
    public boolean isSent(String email) {
        return fromMember.getEmail().equals(email);
    }

    /**
     * 해당 유저가 보낸 메일인지 검사합니다.
     *
     * @param memberId 해당 유저 식별자
     * @return boolean
     */
    public boolean isSent(Long memberId) {
        return fromMember.getId().equals(memberId);
    }
}
