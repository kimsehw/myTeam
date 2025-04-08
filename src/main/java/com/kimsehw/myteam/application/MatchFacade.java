package com.kimsehw.myteam.application;

import com.kimsehw.myteam.dto.match.MatchInviteFormDto;
import com.kimsehw.myteam.dto.match.MatchListResponse;
import com.kimsehw.myteam.dto.match.MatchSearchDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.service.MatchService;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamService;
import jakarta.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log
public class MatchFacade {

    private final MemberService memberService;
    private final MatchService matchService;
    private final TeamService teamService;

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

    public void invite(Principal principal, MatchInviteFormDto matchInviteFormDto) {

    }

    public void validateInviteForm(MatchInviteFormDto matchInviteFormDto, Map<String, String> errors) {
        if (!matchInviteFormDto.getIsNotUser()) {
            validateInviteeTeamInfo(matchInviteFormDto, errors);
        }
        validateMatchDateTime(matchInviteFormDto, errors);
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

    private void validateInviteeTeamInfo(MatchInviteFormDto matchInviteFormDto, Map<String, String> errors) {
        String fieldName = "invitee";
        try {
            memberService.isMyTeam(matchInviteFormDto.getInviteeEmail(), matchInviteFormDto.getInviteeTeamId(),
                    matchInviteFormDto.getInviteeTeamName());
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            errors.put(fieldName, e.getMessage());
        }
    }
}
