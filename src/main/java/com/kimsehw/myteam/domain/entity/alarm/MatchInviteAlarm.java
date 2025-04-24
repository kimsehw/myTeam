package com.kimsehw.myteam.domain.entity.alarm;

import com.kimsehw.myteam.constant.alarm.AlarmType;
import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.domain.utill.DateTimeUtil;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("match_invite")
public class MatchInviteAlarm extends Alarm {

    public static final String RECEIVE_MATCH_INVITE_SUMMARY_TEMPLATE = "%s 팀으로 부터 %s %d시간 %s가 왔습니다.";
    public static final String SENT_MATCH_INVITE_SUMMARY_TEMPLATE = "%s 팀에게 %s를 보냈습니다.";
    public static final String RECEIVE_MATCH_RESPONSE_SUMMARY_TEMPLATE = "%s 팀이 %s %s를 %s 하였습니다.";
    public static final String SENT_RESPONSE_SUMMARY_TEMPLATE = "%s 팀에게 %s %s(을)를 보냈습니다.";

    private LocalDateTime matchDate;

    private boolean response;
    private int matchTime;

    public MatchInviteAlarm(Member fromMember, Member toMember, Team fromTeam, Team toTeam, LocalDateTime matchDate,
                            int matchTime) {
        super(fromMember, toMember, fromTeam, toTeam, AlarmType.MATCH_INVITE);
        this.matchDate = matchDate;
        this.matchTime = matchTime;
    }

    public MatchInviteAlarm(Member fromMember, Member toMember, Team fromTeam, Team toTeam, boolean response) {
        super(fromMember, toMember, fromTeam, toTeam, AlarmType.MATCH_RESPONSE);
        this.response = response;
    }

    @Override
    public String getSummary(boolean isSent) {
        if (getAlarmType().equals(AlarmType.MATCH_INVITE)) {
            return formatSummaryTemplateForInvite(isSent);
        }
        return formatSummaryTemplateForResponse(isSent);
    }

    private String formatSummaryTemplateForInvite(boolean isSent) {
        if (isSent) {
            return String.format(SENT_MATCH_INVITE_SUMMARY_TEMPLATE, getToTeam().getTeamName(),
                    getAlarmType().getTypeName());
        }
        return String.format(RECEIVE_MATCH_INVITE_SUMMARY_TEMPLATE, getFromTeam().getTeamName(),
                DateTimeUtil.formattingToString(matchDate, DateTimeUtil.Y_M_D_H_M_DATE_TYPE), matchTime,
                getAlarmType().getTypeName());
    }

    private String formatSummaryTemplateForResponse(boolean isSent) {
        String response = this.response ? "수락" : "거절";
        if (isSent) {
            return String.format(SENT_RESPONSE_SUMMARY_TEMPLATE, getToTeam().getTeamName(),
                    getAlarmType().getTypeName(),
                    response);
        }
        return String.format(RECEIVE_MATCH_RESPONSE_SUMMARY_TEMPLATE, getFromTeam().getTeamName(),
                DateTimeUtil.formattingToString(matchDate, DateTimeUtil.Y_M_D_H_M_DATE_TYPE),
                getAlarmType().getTypeName(), response);
    }

    @Override
    public String getDetailMessage(boolean isSent) {
        return "";
    }
}
