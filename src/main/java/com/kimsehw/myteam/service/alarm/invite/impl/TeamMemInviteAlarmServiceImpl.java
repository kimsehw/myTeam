package com.kimsehw.myteam.service.alarm.invite.impl;

import com.kimsehw.myteam.domain.entity.alarm.TeamMemInviteAlarm;
import com.kimsehw.myteam.repository.alarm.invite.TeamMemInviteAlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TeamMemInviteAlarmServiceImpl extends AbstractInviteAlarmServiceImpl<TeamMemInviteAlarm> {

    private final TeamMemInviteAlarmRepository teamMemInviteAlarmRepository;

    @Autowired
    public TeamMemInviteAlarmServiceImpl(TeamMemInviteAlarmRepository repository) {
        super(repository);
        teamMemInviteAlarmRepository = repository;
    }
}
