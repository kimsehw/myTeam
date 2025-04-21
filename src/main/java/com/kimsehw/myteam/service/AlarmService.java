package com.kimsehw.myteam.service;

import com.kimsehw.myteam.constant.alarm.AlarmType;
import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.repository.AlarmRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

    private final AlarmRepository alarmRepository;

    @Transactional
    public Long save(Alarm alarm) {
        Alarm save = alarmRepository.save(alarm);
        return save.getId();
    }

    public void validateDuplicateInviteAlarm(Long teamId, Long memberId, Long inviteeId, Map<String, String> errors) {
        List<Long> toMemberIds = alarmRepository.findAllToMemberIdByFromMemberIdAndFromTeamId(
                memberId, teamId, AlarmType.TEAM_MEM_INVITE);
        if (toMemberIds.contains(inviteeId)) {
            errors.put("email", "이미 초대 완료한 회원입니다. 초대 응답을 기다려주세요. (다른 이메일을 입력해주세요.)");
        }
    }
}
