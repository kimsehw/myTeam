package com.kimsehw.myteam.repository.alarm.invite;

import com.kimsehw.myteam.domain.entity.alarm.TeamMemInviteAlarm;
import com.kimsehw.myteam.repository.alarm.AlarmRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface TeamMemInviteAlarmRepository extends AlarmRepository<TeamMemInviteAlarm> {

    @Query("select al.toMember.id from TeamMemInviteAlarm al"
            + " where al.fromMember.id = :fromMemId"
            + " and al.fromTeam.id = :teamId")
    List<Long> findToMemberIdsByFromMemberIdAndFromTeamId(Long fromMemId, Long teamId);
}
