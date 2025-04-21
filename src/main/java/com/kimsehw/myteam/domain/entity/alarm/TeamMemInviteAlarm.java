package com.kimsehw.myteam.domain.entity.alarm;

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

    private int playerNum;

    public TeamMemInviteAlarm(Member fromMember, Member toMember, Team fromTeam, int playerNum) {
        super(fromMember, toMember, fromTeam);
        this.playerNum = playerNum;
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
