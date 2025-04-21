package com.kimsehw.myteam.repository.alarm.invite;

import com.kimsehw.myteam.domain.entity.alarm.MatchInviteAlarm;
import com.kimsehw.myteam.repository.alarm.AlarmRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface MatchInviteAlarmRepository extends AlarmRepository<MatchInviteAlarm> {

    @Query("select al.toMember.id from MatchInviteAlarm al"
            + " where al.fromMember.id = :fromMemId"
            + " and al.fromTeam.id = :teamId")
    List<Long> findToMemberIdsByFromMemberIdAndFromTeamId(Long fromMemId, Long teamId);
}
