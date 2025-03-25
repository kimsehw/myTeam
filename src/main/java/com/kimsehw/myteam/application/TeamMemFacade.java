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
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Log
public class TeamMemFacade {

    private final TeamMemberService teamMemberService;
    private final TeamService teamService;
    private final MemberService memberService;
    private final AlarmService alarmService;


    public void validateInviteInfo(Long teamId, TeamMemInviteFormDto teamMemInviteFormDto, Map<String, String> errors) {
        teamMemberService.validatePlayerNum(teamId, teamMemInviteFormDto, errors);

        if (teamMemInviteFormDto.isNotUser()) {
            validateNotUserInfo(teamMemInviteFormDto, errors);
            return;
        }
        validateOfInviteeEmail(teamMemInviteFormDto, errors); // 자기 자신 경우 추가
    }

    private static void validateNotUserInfo(TeamMemInviteFormDto teamMemInviteFormDto, Map<String, String> errors) {
        if (!StringUtils.hasText(teamMemInviteFormDto.getName())) {
            errors.put("name", "이름을 입력해주세요");
        }
        if (teamMemInviteFormDto.getPosition() == null) {
            errors.put("position", "포지션을 선택해주세요");
        }
    }

    private void validateOfInviteeEmail(TeamMemInviteFormDto teamMemInviteFormDto, Map<String, String> errors) {
        String emailOfInvitee = teamMemInviteFormDto.getEmail();
//        log.info("emailOfInvitee: " + emailOfInvitee);

        if (!StringUtils.hasText(emailOfInvitee)) {
            errors.put("email", "초대 회원의 이메일을 입력해주세요");
            return;
        }

        try {
            Member invitee = memberService.findMemberByEmail(emailOfInvitee);
        } catch (EntityNotFoundException e) {
            errors.put("email", "존재하지 않는 회원입니다. 초대 회원의 이메일을 확인해주세요");
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
        teamMemberService.addTeamMemberIn(teamService.findById(teamId), teamMemInviteFormDto.getName(), TeamRole.MEMBER,
                teamMemInviteFormDto.getPlayerNum(), teamMemInviteFormDto.getPosition());
    }
}
