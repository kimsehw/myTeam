package com.kimsehw.myteam.service.alarm.invite.impl;

import com.kimsehw.myteam.domain.entity.alarm.MatchInviteAlarm;
import com.kimsehw.myteam.repository.alarm.invite.MatchInviteAlarmRepository;
import java.util.List;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Log
public class MatchInviteAlarmServiceImpl extends AbstractInviteAlarmServiceImpl<MatchInviteAlarm> {

    private final MatchInviteAlarmRepository matchInviteAlarmRepository;

    @Autowired
    public MatchInviteAlarmServiceImpl(MatchInviteAlarmRepository repository) {
        super(repository);
        matchInviteAlarmRepository = repository;
    }

    @Override
    public void validateDuplicateInvite(Long teamId, Long memberId, Long inviteeTeamId) {
        List<Long> toTeamIds = matchInviteAlarmRepository.findToTeamIdsByFromMemberIdAndFromTeamId(memberId, teamId);
//        log.info("MatchInviteAlarmServiceImpl " + toTeamIds.toString());
        if (toTeamIds.contains(inviteeTeamId)) {
            throw new IllegalArgumentException(DUPLICATE_MEMBER_INVITE);
        }
    }
}
