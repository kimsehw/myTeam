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
@DiscriminatorValue("response")
public class ResponseAlarm extends Alarm {

    private boolean response;

    public ResponseAlarm(Member fromMember, Member toMember, Team fromTeam, boolean response) {
        super(fromMember, toMember, fromTeam);
        this.response = response;
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
