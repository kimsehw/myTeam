package com.kimsehw.myteam.repository.alarm.invite.team;

import com.kimsehw.myteam.domain.entity.alarm.TeamMemInviteAlarm;
import com.kimsehw.myteam.repository.alarm.basic.BasicAlarmRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface TeamMemInviteAlarmRepository extends BasicAlarmRepository<TeamMemInviteAlarm>,
        TeamMemInviteAlarmRepositoryCustom {

    @Query("select al.toMember.id from TeamMemInviteAlarm al"
            + " where al.fromMember.id = :fromMemId"
            + " and al.fromTeam.id = :teamId")
    List<Long> findToMemberIdsByFromMemberIdAndFromTeamId(Long fromMemId, Long teamId);
}
