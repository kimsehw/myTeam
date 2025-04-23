package com.kimsehw.myteam.controller;

import com.kimsehw.myteam.application.AlarmFacade;
import com.kimsehw.myteam.dto.CustomPage;
import com.kimsehw.myteam.dto.alarm.AlarmDto;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log
public class AlarmApiController {

    public static final int MAX_ALARM_SHOW = 10;
    private final AlarmFacade alarmFacade;

    @GetMapping("/alarms")
    public ResponseEntity myAlarms(Principal principal, @RequestParam(value = "page", defaultValue = "0") int page,
                                   @ModelAttribute AlarmSearchDto alarmSearchDto) {
        String email = principal.getName();
        Pageable pageable = PageRequest.of(page, MAX_ALARM_SHOW);
        Map<String, String> errors = new HashMap<>();

        Page<AlarmDto> alarms = alarmFacade.getMyAlarmPages(alarmSearchDto, email, pageable);
        log.info("done");
        return ResponseEntity.ok(new CustomPage<>(alarms));
    }
}
