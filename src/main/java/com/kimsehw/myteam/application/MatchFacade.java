package com.kimsehw.myteam.application;

import com.kimsehw.myteam.domain.entity.Alarm;
import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.TeamMember;
import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.dto.match.AddMemberFormDto;
import com.kimsehw.myteam.dto.match.MatchInviteFormDto;
import com.kimsehw.myteam.dto.match.MatchListResponse;
import com.kimsehw.myteam.dto.match.MatchSearchDto;
import com.kimsehw.myteam.dto.match.MatchUpdateFormDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.service.AlarmService;
import com.kimsehw.myteam.service.MatchService;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamMemberService;
import com.kimsehw.myteam.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log
public class MatchFacade {

    public static final String ADD_MEMBER_ERROR = "유효하지 않은 팀원이 참여 인원에 추가 요청 되었습니다. 다시 시도해주세요.";
    private final MemberService memberService;
    private final MatchService matchService;
    private final TeamService teamService;
    private final TeamMemberService teamMemberService;
    private final AlarmService alarmService;

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
        if (!matchInviteFormDto.getIsNotUser()) {
            Member fromMember = memberService.getMemberBy(email);
            Member toMember = memberService.getMemberBy(matchInviteFormDto.getInviteeEmail());
            alarmService.save(
                    Alarm.createMatchInviteAlarm(fromMember, toMember, myTeamId, matchInviteFormDto));
            return;
        }
        Team myTeam = teamService.findById(myTeamId);
        matchService.addMatchOn(myTeam, matchInviteFormDto.getInviteeTeamName(), matchInviteFormDto.getMatchDate(),
                matchInviteFormDto.getMatchTime());
    }

    public void validateInviteForm(MatchInviteFormDto matchInviteFormDto, Map<String, String> errors) {
        validateInviteeTeam(matchInviteFormDto, errors);
        validateMatchDateTime(matchInviteFormDto, errors);
    }

    private void validateInviteeTeam(MatchInviteFormDto matchInviteFormDto, Map<String, String> errors) {
        if (!matchInviteFormDto.getIsNotUser()) {
            validateInviteeTeamInfo(matchInviteFormDto, errors);
            return;
        }
        String inviteeTeamName = matchInviteFormDto.getInviteeTeamName();
        log.info(inviteeTeamName);
        if (inviteeTeamName == null || inviteeTeamName.isBlank()) {
            errors.put("inviteeTeamName", "팀 이름을 입력해주세요.");
        }
    }

    private void validateInviteeTeamInfo(MatchInviteFormDto matchInviteFormDto, Map<String, String> errors) {
        String fieldName = "invitee";
        try {
            memberService.isMyTeam(matchInviteFormDto.getInviteeEmail(), matchInviteFormDto.getInviteeTeamId(),
                    matchInviteFormDto.getInviteeTeamName());
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            errors.put(fieldName, e.getMessage());
        }
    }

    private void validateMatchDateTime(MatchInviteFormDto matchInviteFormDto, Map<String, String> errors) {
        validateMatchTime(matchInviteFormDto, errors);
        validateMatchDate(matchInviteFormDto, errors);
    }

    private void validateMatchTime(MatchInviteFormDto matchInviteFormDto, Map<String, String> errors) {
        String fieldName = "matchTime";
        try {
            matchService.validateMatchTime(matchInviteFormDto.getMatchTime());
        } catch (IllegalArgumentException e) {
            errors.put(fieldName, e.getMessage());
        }
    }

    private void validateMatchDate(MatchInviteFormDto matchInviteFormDto, Map<String, String> errors) {
        String fieldName = "matchDate";
        try {
            matchService.validateMatchDate(matchInviteFormDto.getMatchDate());
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
}
