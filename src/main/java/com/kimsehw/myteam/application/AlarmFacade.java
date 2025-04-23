package com.kimsehw.myteam.application;

import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.dto.alarm.AlarmDto;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.alarm.AlarmService;
import com.kimsehw.myteam.service.alarm.factory.AlarmServiceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmFacade {

    private final AlarmServiceFactory alarmServiceFactory;
    private final MemberService memberService;

    public Page<AlarmDto> getMyAlarmPages(AlarmSearchDto alarmSearchDto, String email, Pageable pageable) {
        Member member = memberService.getMemberByEmail(email);
        AlarmService alarmService = alarmServiceFactory.getService(alarmSearchDto.getAlarmType());
        return alarmService.getMyAlarms(alarmSearchDto, member.getId(), pageable);
    }
}
