package com.kimsehw.myteam.application;

import com.kimsehw.myteam.constant.TeamRole;
import com.kimsehw.myteam.dto.teammember.TeamMemInviteFormDto;
import com.kimsehw.myteam.entity.Alarm;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.service.AlarmService;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamMemberService;
import com.kimsehw.myteam.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
@RequiredArgsConstructor
@Log
public class TeamMemFacade {

    private final TeamMemberService teamMemberService;
    private final TeamService teamService;
    private final MemberService memberService;
    private final AlarmService alarmService;


    public void validateInviteInfo(Long teamId, TeamMemInviteFormDto teamMemInviteFormDto,
                                   BindingResult bindingResult) {
        teamMemberService.validatePlayerNum(teamId, teamMemInviteFormDto, bindingResult);
        if (teamMemInviteFormDto.isNotUser()) {
            return;
        }
        validateOfInviteeEmail(teamMemInviteFormDto, bindingResult); // 자기 자신 경우 추가
    }

    private void validateOfInviteeEmail(TeamMemInviteFormDto teamMemInviteFormDto, BindingResult bindingResult) {
        String emailOfInvitee = teamMemInviteFormDto.getEmail();
        log.info("emailOfInvitee: " + emailOfInvitee);

        if (!StringUtils.hasText(emailOfInvitee)) {
            bindingResult.addError(new FieldError("teamMemInviteInfo", "email", "초대 회원의 이메일을 입력해주세요"));
            return;
        }

        try {
            Member invitee = memberService.findMemberByEmail(emailOfInvitee);
        } catch (EntityNotFoundException e) {
            bindingResult.addError(new FieldError("teamMemInviteInfo", "email", "존재하지 않는 회원입니다. 초대 회원의 이메일을 확인해주세요"));
        }
    }

    public void invite(String email, Long teamId, TeamMemInviteFormDto teamMemInviteFormDto) {
        if (!teamMemInviteFormDto.isNotUser()) {
            Member fromMember = memberService.findMemberByEmail(email);
            Member toMember = memberService.findMemberByEmail(teamMemInviteFormDto.getEmail());
            alarmService.save(
                    Alarm.createInviteAlarm(fromMember, toMember, teamId, teamMemInviteFormDto.getPlayerNum()));
            return;
        }
        teamMemberService.addTeamMemberIn(teamService.findById(teamId), null, TeamRole.MEMBER,
                teamMemInviteFormDto.getPlayerNum());
    }
}
