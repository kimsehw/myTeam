package com.kimsehw.myteam.application;

import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.TeamMember;
import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.domain.factory.AlarmFactory;
import com.kimsehw.myteam.domain.utill.DateTimeUtil;
import com.kimsehw.myteam.dto.match.AddMemberFormDto;
import com.kimsehw.myteam.dto.match.MatchInviteFormDto;
import com.kimsehw.myteam.dto.match.MatchListResponse;
import com.kimsehw.myteam.dto.match.MatchSearchDto;
import com.kimsehw.myteam.dto.match.MatchUpdateFormDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.service.MatchService;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamMemberService;
import com.kimsehw.myteam.service.TeamService;
import com.kimsehw.myteam.service.alarm.invite.InviteAlarmService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log
public class MatchFacade {

    public static final String ADD_MEMBER_ERROR = "유효하지 않은 팀원이 참여 인원에 추가 요청 되었습니다. 다시 시도해주세요.";
    public static final String NO_TEAM_NAME_ERROR = "팀 이름을 입력해주세요.";

    private final MemberService memberService;
    private final MatchService matchService;
    private final TeamService teamService;
    private final TeamMemberService teamMemberService;
    private final InviteAlarmService inviteAlarmService;

    public MatchFacade(MemberService memberService, MatchService matchService, TeamService teamService,
                       TeamMemberService teamMemberService,
                       @Qualifier("matchInviteAlarmServiceImpl") InviteAlarmService inviteAlarmService) {
        this.memberService = memberService;
        this.matchService = matchService;
        this.teamService = teamService;
        this.teamMemberService = teamMemberService;
        this.inviteAlarmService = inviteAlarmService;
    }

    /**
     * 검색 조건에 맞춰 해당 회원이 속한 팀들의 일정 정보와 팀 이름 및 아이디를 반환합니다.
     *
     * @param email          회원 아이디
     * @param pageable       페이지 정보
     * @param matchSearchDto 검색 조건
     * @return MatchListResponse
     */
    public MatchListResponse getSearchedMyMatchListResponse(String email, Pageable pageable,
                                                            MatchSearchDto matchSearchDto) {
        List<TeamInfoDto> myTeams;
        try {
            myTeams = memberService.findMyTeamsInfoByEmail(email);
        } catch (EntityNotFoundException e) {
            return new MatchListResponse(null, new PageImpl<>(List.of(), pageable, 0));
        }
        return matchService.getSearchedMatchListResponse(myTeams, matchSearchDto, pageable);
    }

    /**
     * 매치를 추가 혹은 초대 합니다. (비회원 - 매치 추가, 회원 - 매치 초대 알림 생성)
     *
     * @param email              회원 아이디
     * @param matchInviteFormDto 초대 정보
     * @param myTeamId           팀 아이디
     */
    @Transactional
    public void invite(String email, MatchInviteFormDto matchInviteFormDto, Long myTeamId) {
        Team myTeam = teamService.findById(myTeamId);
        if (!matchInviteFormDto.getIsNotUser()) {
            Member fromMember = memberService.getMemberByEmail(email);
            Member toMember = memberService.getMemberByEmail(matchInviteFormDto.getInviteeEmail());
            LocalDateTime matchDate = DateTimeUtil.formatting(matchInviteFormDto.getMatchDate(),
                    DateTimeUtil.Y_M_D_H_M_TYPE);
            Team inviteeTeam = teamService.findById(matchInviteFormDto.getInviteeTeamId());
            inviteAlarmService.send(
                    AlarmFactory.createMatchInviteAlarm(fromMember, toMember, myTeam, inviteeTeam, matchDate,
                            matchInviteFormDto.getMatchTime()));
            return;
        }
        matchService.addNotUserMatchOn(myTeam, matchInviteFormDto.getInviteeTeamName(),
                matchInviteFormDto.getMatchDate(),
                matchInviteFormDto.getMatchTime());
    }

    public void validateInviteForm(String email, Long sessionTeamId, MatchInviteFormDto matchInviteFormDto,
                                   Map<String, String> errors) {
        validateInviteeTeam(email, sessionTeamId, matchInviteFormDto, errors);
        validateMatchDateTime(matchInviteFormDto, errors);
    }

    private void validateInviteeTeam(String email, Long sessionTeamId, MatchInviteFormDto matchInviteFormDto,
                                     Map<String, String> errors) {
        if (!matchInviteFormDto.getIsNotUser()) {
            validateInviteeTeamInfo(email, sessionTeamId, matchInviteFormDto, errors, "invitee");
            return;
        }
        String inviteeTeamName = matchInviteFormDto.getInviteeTeamName();
        log.info(inviteeTeamName);
        if (inviteeTeamName == null || inviteeTeamName.isBlank()) {
            errors.put("inviteeTeamName", NO_TEAM_NAME_ERROR);
        }
    }

    private void validateInviteeTeamInfo(String email, Long sessionTeamId, MatchInviteFormDto matchInviteFormDto,
                                         Map<String, String> errors,
                                         String fieldName) {
        try {
            memberService.isMyTeam(matchInviteFormDto.getInviteeEmail(), matchInviteFormDto.getInviteeTeamId(),
                    matchInviteFormDto.getInviteeTeamName());
            Long memberId = memberService.getMemberByEmail(email).getId();
            inviteAlarmService.validateDuplicateInvite(sessionTeamId, memberId, matchInviteFormDto.getInviteeTeamId());
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            errors.put(fieldName, e.getMessage());
        }
    }

    private void validateMatchDateTime(MatchInviteFormDto matchInviteFormDto, Map<String, String> errors) {
        validateMatchTime(matchInviteFormDto.getMatchTime(), errors, "matchTime");
        validateMatchDate(matchInviteFormDto.getMatchDate(), errors, "matchDate");
    }

    private void validateMatchTime(int matchTime, Map<String, String> errors, String fieldName) {
        try {
            matchService.validateMatchTime(matchTime);
        } catch (IllegalArgumentException e) {
            errors.put(fieldName, e.getMessage());
        }
    }

    private void validateMatchDate(String matchDate, Map<String, String> errors, String fieldName) {
        try {
            matchService.validateMatchDate(matchDate);
        } catch (DateTimeParseException | IllegalArgumentException | NullPointerException e) {
            errors.put(fieldName, e.getMessage());
        }
    }

    public void validateAddMemberIds(List<Long> addMemberIds, Long teamId, Map<String, String> errors) {
        if (!teamService.areTheyInThisTeam(addMemberIds, teamId)) {
            errors.put("errorMessage", ADD_MEMBER_ERROR);
        }
    }

    public void addMember(AddMemberFormDto addMemberFormDto) {
        log.info(addMemberFormDto.toString());
        List<TeamMember> addMembers = teamMemberService.getTeamMembersFrom(addMemberFormDto.getAddMemberIds());
        matchService.addMemberOn(addMemberFormDto.getMatchId(), addMembers);
    }

    public void update(MatchUpdateFormDto matchUpdateFormDto) {
        matchService.updateBy(matchUpdateFormDto);
    }

    public void validateUpdateForm(MatchUpdateFormDto matchUpdateFormDto, Long teamId, String email,
                                   Map<String, String> errors) {
        try {
            memberService.isMyTeam(email, teamId);
            matchService.validateIsMatchOfTeam(matchUpdateFormDto.getId(), teamId);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            errors.put("errorMessage", e.getMessage());
            return;
        }
        validateMatchTime(matchUpdateFormDto.getMatchTime(), errors, "updateMatchTime");
        validateMatchDate(matchUpdateFormDto.getMatchDate(), errors, "updateMatchDate");
        validateUpdateNotUserMatch(matchUpdateFormDto.getIsNotUserMatch(), matchUpdateFormDto.getOpposingTeamName(),
                errors, "updateNotUserTeamName");
    }

    private void validateUpdateNotUserMatch(Boolean isNotUserMatch, String opposingTeamName, Map<String, String> errors,
                                            String fieldName) {
        if (isNotUserMatch && (opposingTeamName == null || opposingTeamName.isBlank())) {
            errors.put(fieldName, NO_TEAM_NAME_ERROR);
        }
    }
}
