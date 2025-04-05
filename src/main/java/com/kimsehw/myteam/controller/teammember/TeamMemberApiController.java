package com.kimsehw.myteam.controller.teammember;

import com.kimsehw.myteam.application.TeamMemFacade;
import com.kimsehw.myteam.dto.teammember.TeamMemInviteFormDto;
import com.kimsehw.myteam.dto.teammember.TeamMemberUpdateRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log
public class TeamMemberApiController {

    public static final String WRONG_TEAM_MEM_ID = "잘못된 팀 멤버가 삭제 요청되었습니다.";
    private final TeamMemFacade teamMemFacade;

    @PostMapping("/teams/{teamId}/team-members/invite")
    public ResponseEntity inviteMember(@PathVariable("teamId") Long teamId, Principal principal,
                                       @RequestBody TeamMemInviteFormDto teamMemInviteFormDto) {
        /*log.info(LocalDateTime.now().toString());
        log.info(teamMemInviteFormDto.toString());*/
        String email = principal.getName();
        Map<String, String> errors = new HashMap<>();

        teamMemFacade.validateInviteInfo(teamId, teamMemInviteFormDto, errors, email);
        if (!errors.keySet().isEmpty()) {
            log.info(errors.toString());
            return ResponseEntity.badRequest().body(errors);
        }

        teamMemFacade.invite(email, teamId, teamMemInviteFormDto);
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @DeleteMapping("/team-members/{teamMemId}")
    public ResponseEntity deleteTeamMem(@PathVariable("teamMemId") Long teamMemId) {
        if (teamMemId == null) {
            return ResponseEntity.badRequest().body(WRONG_TEAM_MEM_ID);
        }
        teamMemFacade.deleteTeamMem(teamMemId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/team-members")
    public ResponseEntity updateTeamMems(@RequestBody TeamMemberUpdateRequest TeamMemberUpdateRequest) {
        Map<Long, Map<String, String>> errors = new HashMap<>();

        teamMemFacade.validateUpdateInfo(TeamMemberUpdateRequest.getTeamMemberUpdateDtos(),
                TeamMemberUpdateRequest.getTeamId(), errors);
        if (!errors.isEmpty()) {
            log.info(errors.entrySet().toString());
            return ResponseEntity.badRequest().body(errors);
        }

        teamMemFacade.updateTeamMems(TeamMemberUpdateRequest.getTeamMemberUpdateDtos());
        return ResponseEntity.ok(Collections.emptyMap());
    }
}
