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
    public static final String RECEIVE_TEAM_INVITE_RESPONSE_SUMMARY_TEMPLATE = "%s 님이 %s를 %s 하였습니다.";
    public static final String SENT_RESPONSE_SUMMARY_TEMPLATE = "%s 팀에게 %s %s(을)를 보냈습니다.";

    private int playerNum;
    private Boolean response;

    public TeamMemInviteAlarm(Member fromMember, Member toMember, Team fromTeam, int playerNum) {
        super(fromMember, toMember, fromTeam, AlarmType.TEAM_INVITE);
        this.playerNum = playerNum;
    }

    public TeamMemInviteAlarm(Member fromMember, Member toMember, Team toTeam, boolean response) {
        super(fromMember, toMember, toTeam, AlarmType.TEAM_INVITE_RESPONSE);
        this.response = response;
    }

    @Override
    public String getSummary(boolean isSent) {
        if (getAlarmType().equals(AlarmType.TEAM_INVITE)) {
            return formatSummaryTemplateForInvite(isSent);
        }
        return formatSummaryTemplateForResponse(isSent);
    }

    private String formatSummaryTemplateForInvite(boolean isSent) {
        if (isSent) {
            return String.format(SENT_TEAM_INVITE_SUMMARY_TEMPLATE, getToMember().getName(),
                    getToMember().getEmail(), getFromTeam().getTeamName(), getAlarmType().getTypeName());
        }
        return String.format(RECEIVE_TEAM_INVITE_SUMMARY_TEMPLATE, getFromTeam().getTeamName(),
                getAlarmType().getTypeName());
    }

    private String formatSummaryTemplateForResponse(boolean isSent) {
        String response = this.response ? "수락" : "거절";
        if (isSent) {
            return String.format(SENT_RESPONSE_SUMMARY_TEMPLATE, getToTeam().getTeamName(),
                    getAlarmType().getTypeName(), response);
        }
        return String.format(RECEIVE_TEAM_INVITE_RESPONSE_SUMMARY_TEMPLATE, getFromMember().getName(),
                getAlarmType().getTypeName(), response);
    }

    @Override
    public String getDetailMessage(boolean isSent) {
        return "";
    }
}
