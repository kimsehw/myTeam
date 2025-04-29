package com.kimsehw.myteam.repository.alarm.invite.match;

import com.kimsehw.myteam.domain.entity.alarm.MatchInviteAlarm;
import com.kimsehw.myteam.repository.alarm.basic.BasicAlarmRepositoryCustom;
import java.util.List;

public interface MatchInviteAlarmRepositoryCustom extends BasicAlarmRepositoryCustom<MatchInviteAlarm> {
    List<Long> findToTeamIdsByFromMemberIdAndFromTeamId(Long fromMemId, Long teamId);
}
