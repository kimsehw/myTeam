package com.kimsehw.myteam.service.alarm.invite.impl;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.repository.alarm.AlarmRepository;
import com.kimsehw.myteam.service.alarm.impl.AbstractAlarmServiceImpl;
import com.kimsehw.myteam.service.alarm.invite.InviteAlarmService;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public abstract class AbstractInviteAlarmServiceImpl<T extends Alarm> extends AbstractAlarmServiceImpl<T> implements
        InviteAlarmService {
    public static final String DUPLICATE_MEMBER_INVITE = "이미 초대 완료한 회원입니다. 초대 응답을 기다려주세요.";

    private final AlarmRepository<T> alarmRepository;

    public AbstractInviteAlarmServiceImpl(AlarmRepository<T> repository) {
        super(repository);
        alarmRepository = repository;
    }

    @Override
    public void validateDuplicateInvite(Long teamId, Long memberId, Long inviteeId) {
        List<Long> toMemberIds = alarmRepository.findToMemberIdsByFromMemberIdAndFromTeamId(memberId, teamId);
        if (toMemberIds.contains(inviteeId)) {
            throw new IllegalArgumentException(DUPLICATE_MEMBER_INVITE);
        }
    }
}
