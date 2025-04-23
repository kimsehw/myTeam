package com.kimsehw.myteam.service.alarm.invite;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.service.alarm.AlarmService;

public interface InviteAlarmService<T extends Alarm> extends AlarmService<T> {
    void validateDuplicateInvite(Long teamId, Long memberId, Long inviteeId);
}
