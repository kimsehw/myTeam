package com.kimsehw.myteam.service.alarm.invite;

import com.kimsehw.myteam.service.alarm.AlarmService;

public interface InviteAlarmService extends AlarmService {
    void validateDuplicateInvite(Long teamId, Long memberId, Long inviteeId);
}
