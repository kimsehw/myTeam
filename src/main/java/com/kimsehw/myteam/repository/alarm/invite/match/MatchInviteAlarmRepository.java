package com.kimsehw.myteam.repository.alarm.invite.match;

import com.kimsehw.myteam.domain.entity.alarm.MatchInviteAlarm;
import com.kimsehw.myteam.repository.alarm.basic.BasicAlarmRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface MatchInviteAlarmRepository extends BasicAlarmRepository<MatchInviteAlarm>,
        MatchInviteAlarmRepositoryCustom {

    @Query("select al.toTeam.id from MatchInviteAlarm al"
            + " where al.fromMember.id = :fromMemId"
            + " and al.fromTeam.id = :teamId")
    List<Long> findToTeamIdsByFromMemberIdAndFromTeamId(Long fromMemId, Long teamId);
}
