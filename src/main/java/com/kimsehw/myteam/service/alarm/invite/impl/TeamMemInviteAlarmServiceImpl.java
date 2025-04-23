package com.kimsehw.myteam.service.alarm.invite.impl;

import com.kimsehw.myteam.domain.entity.alarm.TeamMemInviteAlarm;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import com.kimsehw.myteam.repository.alarm.invite.team.TeamMemInviteAlarmRepository;
import java.util.List;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Log
public class TeamMemInviteAlarmServiceImpl extends AbstractInviteAlarmServiceImpl<TeamMemInviteAlarm> {

    private final TeamMemInviteAlarmRepository teamMemInviteAlarmRepository;

    @Autowired
    public TeamMemInviteAlarmServiceImpl(TeamMemInviteAlarmRepository repository) {
        super(repository);
        teamMemInviteAlarmRepository = repository;
    }

    @Override
    public void validateDuplicateInvite(Long teamId, Long memberId, Long inviteeId) {
        List<Long> toMemberIds = teamMemInviteAlarmRepository.findToMemberIdsByFromMemberIdAndFromTeamId(memberId,
                teamId);
//        log.info("TeamMemInviteAlarmServiceImpl " + toMemberIds.toString());
        if (toMemberIds.contains(inviteeId)) {
            throw new IllegalArgumentException(DUPLICATE_MEMBER_INVITE);
        }
    }

    @Override
    public Page<TeamMemInviteAlarm> searchAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable) {
        return teamMemInviteAlarmRepository.searchAlarms(alarmSearchDto, memberId, pageable);
    }
}
