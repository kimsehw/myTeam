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
@DiscriminatorValue("response")
public class ResponseAlarm extends Alarm {
    public static final String RECEIVE_MATCH_RESPONSE_SUMMARY_TEMPLATE = "%s 팀이 %s을 %s 하였습니다.";
    public static final String RECEIVE_TEAM_INVITE_RESPONSE_SUMMARY_TEMPLATE = "%s 님이 %s을 %s 하였습니다.";
    public static final String SENT_RESPONSE_SUMMARY_TEMPLATE = "%s 팀에게 %s %s(을)를 보냈습니다.";

    private boolean response;

    public ResponseAlarm(Member fromMember, Member toMember, Team fromTeam, Team toTeam, AlarmType responseAlarmType,
                         boolean response) {
        super(fromMember, toMember, fromTeam, toTeam, responseAlarmType);
        this.response = response;
    }

    public ResponseAlarm(Member fromMember, Member toMember, Team toTeam, AlarmType responseAlarmType,
                         boolean response) {
        super(fromMember, toMember, toTeam, responseAlarmType);
        this.response = response;
    }

    @Override
    public String getSummary(boolean isSent) {
        return formatSummaryTemplate(isSent);
    }

    private String formatSummaryTemplate(boolean isSent) {
        String response = this.response ? "수락" : "거절";
        if (isSent) {
            return String.format(SENT_RESPONSE_SUMMARY_TEMPLATE, getToTeam().getTeamName(),
                    getAlarmType().getTypeName(),
                    response);
        }
        if (isMatchResponse()) {
            return String.format(RECEIVE_MATCH_RESPONSE_SUMMARY_TEMPLATE, getFromTeam().getTeamName(),
                    getAlarmType().getTypeName(), response);
        }
        return String.format(RECEIVE_TEAM_INVITE_RESPONSE_SUMMARY_TEMPLATE, getFromMember().getName(),
                getAlarmType().getTypeName(), response);
    }

    @Override
    public String getDetailMessage(boolean isSent) {
        return formatSummaryTemplate(isSent);
    }

    private boolean isMatchResponse() {
        return getAlarmType().equals(AlarmType.MATCH_RESPONSE);
    }
}
