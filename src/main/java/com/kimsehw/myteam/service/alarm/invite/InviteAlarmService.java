package com.kimsehw.myteam.service.alarm.invite;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.dto.alarm.AlarmResponseFormDto;
import com.kimsehw.myteam.service.alarm.AlarmService;

public interface InviteAlarmService<T extends Alarm> extends AlarmService<T> {
    void validateDuplicateInvite(Long teamId, Long memberId, Long inviteeId);

    /**
     * 알람 응답 정보에 대한 유효성 검사
     *
     * @param inviteAlarm          응답하고자 하는 알람
     * @param responseMemberId     응답 요청한 회원 아이디
     * @param alarmResponseFormDto 응답 알람 생성 정보
     * @throws IllegalArgumentException 잘못된 응답 요청
     */
    void validateResponseForm(T inviteAlarm, Long responseMemberId, AlarmResponseFormDto alarmResponseFormDto);
}
