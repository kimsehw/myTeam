package com.kimsehw.myteam.application;

import com.kimsehw.myteam.constant.TeamRole;
import com.kimsehw.myteam.dto.teammember.TeamMemInviteFormDto;
import com.kimsehw.myteam.dto.teammember.TeamMemberDetailDto;
import com.kimsehw.myteam.dto.teammember.TeamMemberDto;
import com.kimsehw.myteam.dto.teammember.TeamMemberUpdateDto;
import com.kimsehw.myteam.entity.Alarm;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.service.AlarmService;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamMemberService;
import com.kimsehw.myteam.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    public void validateInviteInfo(Long teamId, TeamMemInviteFormDto teamMemInviteFormDto, Map<String, String> errors,
                                   String email) {
        teamMemberService.validatePlayerNum(teamId, teamMemInviteFormDto, errors);

        if (teamMemInviteFormDto.isNotUser()) {
            validateNotUserInfo(teamMemInviteFormDto, errors);
            return;
        }
        Long inviteeId = validateOfInviteeEmail(teamMemInviteFormDto, errors, email);
        if (inviteeId != -1) {
            Member member = memberService.findMemberByEmail(email);
            alarmService.validateDuplicateInviteAlarm(teamId, member.getId(), inviteeId, errors);
        }

    }

    private static void validateNotUserInfo(TeamMemInviteFormDto teamMemInviteFormDto, Map<String, String> errors) {
        if (!StringUtils.hasText(teamMemInviteFormDto.getName())) {
            errors.put("name", "이름을 입력해주세요");
        }
        if (teamMemInviteFormDto.getPosition() == null) {
            errors.put("position", "포지션을 선택해주세요");
        }
    }

    private Long validateOfInviteeEmail(TeamMemInviteFormDto teamMemInviteFormDto, Map<String, String> errors,
                                        String email) {
        String emailOfInvitee = teamMemInviteFormDto.getEmail();
//        log.info("emailOfInvitee: " + emailOfInvitee);

        if (!StringUtils.hasText(emailOfInvitee)) {
            errors.put("email", "초대 회원의 이메일을 입력해주세요.");
            return -1L;
        }
        if (emailOfInvitee.equals(email)) {
            errors.put("email", "올바르지 않은 이메일 입니다. 자기 자신은 초대할 수 없습니다. 이메일을 확인해주세요.");
            return -1L;
        }
        Member invitee;
        try {
            invitee = memberService.findMemberByEmail(emailOfInvitee);
        } catch (EntityNotFoundException e) {
            errors.put("email", "존재하지 않는 회원입니다. 초대 회원의 이메일을 확인해주세요.");
            return -1L;
        }
        return invitee.getId();
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

    public void deleteTeamMem(Long teamMemId) {
        teamMemberService.deleteTeamMemById(teamMemId);
    }

    public Page<TeamMemberDto> getTeamMemberDtoPagesOf(Long teamId, Pageable pageable) {
        return teamMemberService.getTeamMemberDtoPagesOf(teamId, pageable);
    }

    public TeamMemberDetailDto getTeamMemberDetailDto(Long teamMemId) {
        return teamMemberService.getTeamMemberDetailDto(teamMemId);
    }

    public boolean isAuthorizeToManageTeam(Principal principal, Long teamId) {
        Member member = memberService.findMemberByEmail(principal.getName());
        return teamMemberService.isAuthorizeMemberToManageTeam(member.getId(), teamId);
    }

    public void updateTeamMems(List<TeamMemberUpdateDto> teamMemberUpdateDtos) {
        teamMemberService.updateTeamMems(teamMemberUpdateDtos);
    }
}
