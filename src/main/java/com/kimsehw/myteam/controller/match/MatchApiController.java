package com.kimsehw.myteam.controller.match;

import com.kimsehw.myteam.application.MatchFacade;
import com.kimsehw.myteam.dto.match.MatchInviteFormDto;
import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log
public class MatchApiController {

    public final MatchFacade matchFacade;

    @PostMapping("/matches/invite")
    public ResponseEntity invite(Model model, @RequestBody MatchInviteFormDto matchInviteFormDto, Principal principal,
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
}
