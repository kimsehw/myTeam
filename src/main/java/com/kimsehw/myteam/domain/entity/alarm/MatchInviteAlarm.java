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

    public static final String MATCH_INVITE_SUMMARY_TEMPLATE = "%s 팀으로 부터 %s %s가 왔습니다.";
    private LocalDateTime matchDate;

    private int matchTime;

    public MatchInviteAlarm(Member fromMember, Member toMember, Team fromTeam, Team toTeam, LocalDateTime matchDate,
                            int matchTime) {
        super(fromMember, toMember, fromTeam, toTeam);
        this.matchDate = matchDate;
        this.matchTime = matchTime;
    }

    @Override
    public String getSummary() {
        return formatSummaryTemplate();
    }

    private String formatSummaryTemplate() {
        return String.format(MATCH_INVITE_SUMMARY_TEMPLATE, getFromTeam().getTeamName(),
                DateTimeUtil.formattingToString(matchDate, DateTimeUtil.Y_M_D_TYPE), getType().getTypeName());
    }

    @Override
    public String getDetailMessage() {
        return "";
    }

    @Override
    public AlarmType getType() {
        return AlarmType.MATCH_INVITE;
    }
}
