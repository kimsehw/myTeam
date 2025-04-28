package com.kimsehw.myteam.service.alarm.invite.impl;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.dto.alarm.AlarmResponseFormDto;
import com.kimsehw.myteam.repository.alarm.basic.BasicAlarmRepository;
import com.kimsehw.myteam.service.alarm.basic.AbstractBasicAlarmServiceImpl;
import com.kimsehw.myteam.service.alarm.invite.InviteAlarmService;
import lombok.extern.java.Log;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Log
public abstract class AbstractInviteAlarmServiceImpl<T extends Alarm> extends
        AbstractBasicAlarmServiceImpl<T> implements
        InviteAlarmService<T> {
    public static final String DUPLICATE_MEMBER_INVITE = "이미 초대 완료한 회원입니다. 초대 응답을 기다려주세요.";
    public static final String NO_AUTH_ALARM = "해당 알람에 대한 응답 권한이 없습니다. 알람 정보를 확인해주세요.";
    public static final String RESPONSE_FORM_ERROR = "해당 초대 알람에 대한 잘못된 응답 알람 정보입니다. 알람 정보를 확인해주세요";

    public AbstractInviteAlarmServiceImpl(BasicAlarmRepository<T> repository) {
        super(repository);
    }

    @Override
    public abstract void validateDuplicateInvite(Long teamId, Long memberId, Long inviteeInfoId);

    @Override
    public void validateResponseForm(T inviteAlarm, Long responseMemberId, AlarmResponseFormDto alarmResponseFormDto) {
        if (!inviteAlarm.isFor(responseMemberId)) {
            throw new IllegalArgumentException(NO_AUTH_ALARM);
        }
        if (!isRightResponseFormFor(inviteAlarm, alarmResponseFormDto)) {
            throw new IllegalArgumentException(RESPONSE_FORM_ERROR);
        }

    }

    private boolean isRightResponseFormFor(T inviteAlarm, AlarmResponseFormDto alarmResponseFormDto) {
        return inviteAlarm.isFrom(alarmResponseFormDto.getToMemId()) &&
                inviteAlarm.isFor(alarmResponseFormDto.getFromMemId());
    }

    @Transactional
    @Override
    public void deleteOrHide(T alarm, String email) {
        if (alarm.isResponseType()) {
            super.deleteOrHide(alarm, email);
            return;
        }
        log.info("inviteAlarmDelete");
        if (alarm.isFrom(email)) {
            if (!alarm.isReadByToMember()) {
                delete(alarm);
                return;
            }
            if (alarm.isHideByToMember()) {
                delete(alarm);
                return;
            }
            throw new IllegalArgumentException(CANT_DELETE_MY_ALARM);
        }
        alarm.hide(email);
        log.info(email + "can just hide");
    }
}
