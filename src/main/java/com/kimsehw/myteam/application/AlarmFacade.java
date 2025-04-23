package com.kimsehw.myteam.application;

import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.dto.alarm.AlarmDto;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.alarm.AlarmService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlarmFacade {

    private final AlarmService alarmService;
    private final MemberService memberService;

    public AlarmFacade(@Qualifier("alarmServiceImpl") AlarmService alarmService, MemberService memberService) {
        this.alarmService = alarmService;
        this.memberService = memberService;
    }

    public Page<AlarmDto> getMyAlarmPages(AlarmSearchDto alarmSearchDto, String email, Pageable pageable) {
        Member member = memberService.getMemberByEmail(email);
        return alarmService.getMyAlarms(alarmSearchDto, member.getId(), pageable);
    }
}
