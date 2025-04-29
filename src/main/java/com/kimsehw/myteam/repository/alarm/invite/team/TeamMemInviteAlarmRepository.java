package com.kimsehw.myteam.repository.alarm.invite.team;

import com.kimsehw.myteam.domain.entity.alarm.TeamMemInviteAlarm;
import com.kimsehw.myteam.repository.alarm.basic.BasicAlarmRepository;

public interface TeamMemInviteAlarmRepository extends BasicAlarmRepository<TeamMemInviteAlarm>,
        TeamMemInviteAlarmRepositoryCustom {
}
