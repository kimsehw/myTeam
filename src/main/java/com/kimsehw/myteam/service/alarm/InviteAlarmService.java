package com.kimsehw.myteam.service.alarm;

public interface InviteAlarmService extends AlarmService {
    void validateDuplicateInvite(Long teamId, Long memberId, Long inviteeId);
}
