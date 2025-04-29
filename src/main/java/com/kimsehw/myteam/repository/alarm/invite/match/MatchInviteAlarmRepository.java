package com.kimsehw.myteam.repository.alarm.invite.match;

import com.kimsehw.myteam.domain.entity.alarm.MatchInviteAlarm;
import com.kimsehw.myteam.repository.alarm.basic.BasicAlarmRepository;

public interface MatchInviteAlarmRepository extends BasicAlarmRepository<MatchInviteAlarm>,
        MatchInviteAlarmRepositoryCustom {
}
