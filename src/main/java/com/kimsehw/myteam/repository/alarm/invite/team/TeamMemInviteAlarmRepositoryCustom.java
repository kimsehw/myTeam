package com.kimsehw.myteam.repository.alarm.invite.team;

import com.kimsehw.myteam.domain.entity.alarm.TeamMemInviteAlarm;
import com.kimsehw.myteam.repository.alarm.basic.BasicAlarmRepositoryCustom;
import java.util.List;

public interface TeamMemInviteAlarmRepositoryCustom extends BasicAlarmRepositoryCustom<TeamMemInviteAlarm> {
    List<Long> findToMemberIdsByFromMemberIdAndFromTeamId(Long fromMemId, Long teamId);
}
