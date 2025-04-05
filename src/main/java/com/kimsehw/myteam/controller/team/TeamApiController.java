package com.kimsehw.myteam.controller.team;

import com.kimsehw.myteam.application.TeamFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequiredArgsConstructor
public class TeamApiController {

    private final TeamFacade teamFacade;

    @DeleteMapping("/teams/{teamId}")
    public ResponseEntity delete(@PathVariable("teamId") Long teamId, @RequestParam("logoUrl") String logoUrl) {

        try {
            teamFacade.deleteTeam(teamId, logoUrl);
        } catch (Exception e) {

        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
