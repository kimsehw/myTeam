package com.kimsehw.myteam.controller.match;

import com.kimsehw.myteam.application.MatchFacade;
import com.kimsehw.myteam.dto.match.AddMemberFormDto;
import com.kimsehw.myteam.dto.match.MatchInviteFormDto;
import com.kimsehw.myteam.dto.match.MatchUpdateFormDto;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log
public class MatchApiController {

    public final MatchFacade matchFacade;

    @PostMapping("/matches/invite")
    public ResponseEntity invite(@RequestBody MatchInviteFormDto matchInviteFormDto, Principal principal,
                                 HttpSession session) {
        log.info("inviteStart");
        Map<String, String> errors = new HashMap<>();
        String email = principal.getName();
        Long sessionTeamId = (Long) session.getAttribute("currentViewTeamId");
        matchFacade.validateInviteForm(matchInviteFormDto, errors);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        matchFacade.invite(email, matchInviteFormDto, sessionTeamId);
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @PostMapping("/matches/addMember")
    public ResponseEntity addMember(@RequestBody AddMemberFormDto addMemberFormDto, HttpSession session) {
        Long teamId = (Long) session.getAttribute("currentViewTeamId");
        Map<String, String> errors = new HashMap<>();
        matchFacade.validateAddMemberIds(addMemberFormDto.getAddMemberIds(), teamId, errors);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        matchFacade.addMember(addMemberFormDto);
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @PatchMapping("/matches/update")
    public ResponseEntity update(@RequestBody MatchUpdateFormDto matchUpdateFormDto, HttpSession session) {
        Long teamId = (Long) session.getAttribute("currentViewTeamId");
        /*
        추후 검증 과정 추가
        Map<String, String> errors = new HashMap<>();
        matchFacade.validateAddMemberIds(addMemberFormDto.getAddMemberIds(), teamId, errors);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }*/
        matchFacade.update(matchUpdateFormDto);
        return ResponseEntity.ok(Collections.emptyMap());
    }
}
