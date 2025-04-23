package com.kimsehw.myteam.service.alarm.invite.impl;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.repository.alarm.basic.BasicAlarmRepository;
import com.kimsehw.myteam.service.alarm.basic.AbstractBasicAlarmServiceImpl;
import com.kimsehw.myteam.service.alarm.invite.InviteAlarmService;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public abstract class AbstractInviteAlarmServiceImpl<T extends Alarm> extends
        AbstractBasicAlarmServiceImpl<T> implements
        InviteAlarmService {
    public static final String DUPLICATE_MEMBER_INVITE = "이미 초대 완료한 회원입니다. 초대 응답을 기다려주세요.";

    public AbstractInviteAlarmServiceImpl(BasicAlarmRepository<T> repository) {
        super(repository);
    }

    @Override
    public abstract void validateDuplicateInvite(Long teamId, Long memberId, Long inviteeInfoId);
}
