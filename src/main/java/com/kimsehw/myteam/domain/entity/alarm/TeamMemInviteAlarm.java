package com.kimsehw.myteam.domain.entity.alarm;

import com.kimsehw.myteam.constant.alarm.AlarmType;
import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.team.Team;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("team_invite")
public class TeamMemInviteAlarm extends Alarm {
    public static final String RECEIVE_TEAM_INVITE_SUMMARY_TEMPLATE = "%s 팀으로 부터 %s가 왔습니다.";
    public static final String SENT_TEAM_INVITE_SUMMARY_TEMPLATE = "%s(%s)님에게 %s팀 %s를 보냈습니다.";

    private int playerNum;

    public TeamMemInviteAlarm(Member fromMember, Member toMember, Team fromTeam, int playerNum) {
        super(fromMember, toMember, fromTeam);
        this.playerNum = playerNum;
    }

    @Override
    public String getSummary(boolean isSent) {
        return formatSummaryTemplate(isSent);
    }

    private String formatSummaryTemplate(boolean isSent) {
        if (isSent) {
            return String.format(SENT_TEAM_INVITE_SUMMARY_TEMPLATE, getToMember().getName(),
                    getToMember().getEmail(), getFromTeam().getTeamName(), getType().getTypeName());
        }
        return String.format(RECEIVE_TEAM_INVITE_SUMMARY_TEMPLATE, getFromTeam().getTeamName(),
                getType().getTypeName());
    }

    @Override
    public String getDetailMessage(boolean isSent) {
        return "";
    }

    @Override
    public AlarmType getType() {
        return AlarmType.TEAM_INVITE;
    }
}
