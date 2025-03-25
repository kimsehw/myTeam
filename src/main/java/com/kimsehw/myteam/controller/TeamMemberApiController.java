package com.kimsehw.myteam.controller;

import com.kimsehw.myteam.application.TeamMemFacade;
import com.kimsehw.myteam.dto.teammember.TeamMemInviteFormDto;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log
public class TeamMemberApiController {

    private final TeamMemFacade teamMemFacade;

    @PostMapping("/teams/{teamId}/team-members/invite")
    public ResponseEntity inviteMember(@PathVariable("teamId") Long teamId, Principal principal,
                                       @RequestBody TeamMemInviteFormDto teamMemInviteFormDto,
                                       BindingResult bindingResult,
                                       Model model) {
        /*log.info(LocalDateTime.now().toString());
        log.info(teamMemInviteFormDto.toString());*/

        teamMemFacade.validateInviteInfo(teamId, teamMemInviteFormDto, bindingResult);
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            Map<String, String> errors = new HashMap<>();

            for (FieldError fieldError : fieldErrors) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
//            log.info(errors.toString());
            return ResponseEntity.badRequest().body(errors);
        }

        String email = principal.getName();
        teamMemFacade.invite(email, teamId, teamMemInviteFormDto);

        return ResponseEntity.ok(Collections.emptyMap());
    }
}
