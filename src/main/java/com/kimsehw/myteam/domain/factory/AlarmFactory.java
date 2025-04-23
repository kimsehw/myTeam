package com.kimsehw.myteam.domain.factory;

import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.alarm.MatchInviteAlarm;
import com.kimsehw.myteam.domain.entity.alarm.ResponseAlarm;
import com.kimsehw.myteam.domain.entity.alarm.TeamMemInviteAlarm;
import com.kimsehw.myteam.domain.entity.team.Team;
import java.time.LocalDateTime;

public class AlarmFactory {

    public static TeamMemInviteAlarm createTeamMemInviteAlarm(Member fromMember, Member toMember, Team fromTeam,
                                                              int playerNum) {
        return new TeamMemInviteAlarm(fromMember, toMember, fromTeam, playerNum);
    }

    public static MatchInviteAlarm createMatchInviteAlarm(Member fromMember, Member toMember, Team fromTeam,
                                                          Team toTeam, LocalDateTime matchDate, int matchTime) {
        return new MatchInviteAlarm(fromMember, toMember, fromTeam, toTeam, matchDate, matchTime);
    }

    public static ResponseAlarm createResponseAlarm(Member fromMember, Member toMember, Team fromTeam, Team toTeam,
                                                    boolean response) {
        return new ResponseAlarm(fromMember, toMember, fromTeam, toTeam, response);
    }
}
