package com.kimsehw.myteam.domain.entity.alarm;

import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.team.Team;
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
        return "";
    }

    @Override
    public String getDetailMessage() {
        return "";
    }
}
