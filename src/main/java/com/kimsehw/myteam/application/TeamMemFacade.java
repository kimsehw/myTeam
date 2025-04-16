package com.kimsehw.myteam.application;

import com.kimsehw.myteam.constant.teammember.TeamRole;
import com.kimsehw.myteam.domain.FieldError;
import com.kimsehw.myteam.domain.entity.Alarm;
import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.TeamMember;
import com.kimsehw.myteam.dto.teammember.TeamMemInviteFormDto;
import com.kimsehw.myteam.dto.teammember.TeamMemberDetailDto;
import com.kimsehw.myteam.dto.teammember.TeamMemberDto;
import com.kimsehw.myteam.dto.teammember.TeamMemberForAddMatchDto;
import com.kimsehw.myteam.dto.teammember.TeamMemberUpdateDto;
import com.kimsehw.myteam.exception.FieldErrorException;
import com.kimsehw.myteam.service.AlarmService;
import com.kimsehw.myteam.service.MatchService;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamMemberService;
import com.kimsehw.myteam.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Log
public class TeamMemFacade {

    public static final String SELF_EMAIL_INPUT_ERROR = "올바르지 않은 이메일 입니다. 자기 자신은 초대할 수 없습니다. 이메일을 확인해주세요.";
    public static final String NO_EMAIL_ERROR = "초대 회원의 이메일을 입력해주세요.";
    public static final String WRONG_EMAIL_ERROR = "존재하지 않는 회원입니다. 초대 회원의 이메일을 확인해주세요.";
    public static final String NO_NAME_INPUT_ERROR = "이름은 필수 입력값입니다.";
    public static final String DUPLICATE_LEADER_ERROR = "회장이 이미 존재합니다. 회장은 한명만 가능합니다.";
    private final TeamMemberService teamMemberService;
    private final TeamService teamService;
    private final MemberService memberService;
    private final AlarmService alarmService;
    private final MatchService matchService;


    public void validateInviteInfo(Long teamId, TeamMemInviteFormDto teamMemInviteFormDto, Map<String, String> errors,
                                   String email) {

        try {
            teamMemberService.validatePlayerNum(teamId, teamMemInviteFormDto.getPlayerNum());
        } catch (FieldErrorException e) {
            addFieldError(errors, e);
        }

        if (teamMemInviteFormDto.isNotUser()) {
            List<FieldError> notUserErrors = validateNotUserInfo(teamMemInviteFormDto);
            for (FieldError notUserError : notUserErrors) {
                errors.put(notUserError.getField(), notUserError.getErrorMessage());
            }
            return;
        }

        try {
            Long inviteeId = validateOfInviteeEmail(teamMemInviteFormDto, email);
            Member member = memberService.getMemberBy(email);
            alarmService.validateDuplicateInviteAlarm(teamId, member.getId(), inviteeId, errors);
        } catch (FieldErrorException e) {
            addFieldError(errors, e);
        }
    }

    private static void addFieldError(Map<String, String> errors, FieldErrorException e) {
        FieldError fieldError = e.getFieldError();
        errors.put(fieldError.getField(), fieldError.getErrorMessage());
    }

    /**
     * 비회원에 대한 초대 정보 유효성 검사를 실시합니다.
     *
     * @param teamMemInviteFormDto
     * @return List<FieldError> or 검사 통과할 경우 null
     */
    private static List<FieldError> validateNotUserInfo(TeamMemInviteFormDto teamMemInviteFormDto) {
        List<FieldError> notUserErrors = new ArrayList<>();
        if (!StringUtils.hasText(teamMemInviteFormDto.getName())) {
            notUserErrors.add(FieldError.of("name", "이름을 입력해주세요"));
        }
        if (teamMemInviteFormDto.getPosition() == null) {
            notUserErrors.add(FieldError.of("position", "포지션을 선택해주세요"));
        }
        return notUserErrors;
    }

    /**
     * 초대 회원의 이메일의 유효성을 검사합니다. 입력 여부 & 자기자신 입력 & 없는 회원
     *
     * @param teamMemInviteFormDto
     * @param email
     * @return 검사 통과할 경우 초대 회원의 memberId
     */
    private Long validateOfInviteeEmail(TeamMemInviteFormDto teamMemInviteFormDto, String email) {
        String emailOfInvitee = teamMemInviteFormDto.getEmail();
//        log.info("emailOfInvitee: " + emailOfInvitee);

        if (!StringUtils.hasText(emailOfInvitee)) {
            throw new FieldErrorException(FieldError.of("email", NO_EMAIL_ERROR));
        }
        if (emailOfInvitee.equals(email)) {
            throw new FieldErrorException(FieldError.of("email", SELF_EMAIL_INPUT_ERROR));
        }
        Member invitee;
        try {
            invitee = memberService.getMemberBy(emailOfInvitee);
        } catch (EntityNotFoundException e) {
            throw new FieldErrorException(FieldError.of("email", WRONG_EMAIL_ERROR));
        }
        return invitee.getId();
    }

    public void invite(String email, Long teamId, TeamMemInviteFormDto teamMemInviteFormDto) {
        if (!teamMemInviteFormDto.isNotUser()) {
            Member fromMember = memberService.getMemberBy(email);
            Member toMember = memberService.getMemberBy(teamMemInviteFormDto.getEmail());
            alarmService.save(
                    Alarm.createTeamMemInviteAlarm(fromMember, toMember, teamId, teamMemInviteFormDto.getPlayerNum()));
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
        Member member = memberService.getMemberBy(principal.getName());
        return teamMemberService.isAuthorizeMemberToManageTeam(member.getId(), teamId);
    }

    public void updateTeamMems(List<TeamMemberUpdateDto> teamMemberUpdateDtos) {
        teamMemberService.updateTeamMems(teamMemberUpdateDtos);
    }

    public void validateUpdateInfo(List<TeamMemberUpdateDto> teamMemberUpdateDtos, Long teamId,
                                   Map<Long, Map<String, String>> errors) {
        teamMemberUpdateDtos.forEach(dto -> {
            HashMap<String, String> error = new HashMap<>();
            noNameInputError(dto, error);
            validatePlayerNumError(teamId, dto, error);
            duplicateLeaderError(dto, error);

            if (!error.isEmpty()) {
                errors.put(dto.getTeamMemId(), error);
            }
        });
    }

    private void duplicateLeaderError(TeamMemberUpdateDto dto, HashMap<String, String> error) {
        try {
            if (dto.getTeamRole() == TeamRole.LEADER && !teamMemberService.isTeamLeader(dto.getTeamMemId())) {
                throw new FieldErrorException(FieldError.of("teamRole", DUPLICATE_LEADER_ERROR));
            }
        } catch (FieldErrorException e) {
            addFieldError(error, e);
        }
    }

    private void validatePlayerNumError(Long teamId, TeamMemberUpdateDto dto, HashMap<String, String> error) {
        try {
            teamMemberService.validatePlayerNum(teamId, dto.getPlayerNum(), dto.getTeamMemId());
        } catch (FieldErrorException e) {
            addFieldError(error, e);
        }
    }

    private static void noNameInputError(TeamMemberUpdateDto dto, HashMap<String, String> error) {
        try {
            if (!StringUtils.hasText(dto.getName())) {
                throw new FieldErrorException(FieldError.of("name", NO_NAME_INPUT_ERROR));
            }
        } catch (FieldErrorException e) {
            addFieldError(error, e);
        }
    }

    public Page<TeamMemberForAddMatchDto> getTeamMemberForAddMatchDtoPagesOf(Long matchId, Long teamId,
                                                                             Pageable pageable) {
        Page<TeamMember> teamMemberPages = teamMemberService.getTeamMembersIn(teamId, pageable);
        return new PageImpl<>(matchService.getTeamMemberForAddMatchDto(matchId, teamMemberPages.getContent()), pageable,
                teamMemberPages.getTotalElements());
    }
}
