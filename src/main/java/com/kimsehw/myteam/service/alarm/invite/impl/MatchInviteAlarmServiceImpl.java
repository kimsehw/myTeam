package com.kimsehw.myteam.service.alarm.invite.impl;

import com.kimsehw.myteam.domain.entity.alarm.MatchInviteAlarm;
import com.kimsehw.myteam.repository.alarm.invite.MatchInviteAlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class MatchInviteAlarmServiceImpl extends AbstractInviteAlarmServiceImpl<MatchInviteAlarm> {

    private final MatchInviteAlarmRepository matchInviteAlarmRepository;

    @Autowired
    public MatchInviteAlarmServiceImpl(MatchInviteAlarmRepository repository) {
        super(repository);
        matchInviteAlarmRepository = repository;
    }
}
