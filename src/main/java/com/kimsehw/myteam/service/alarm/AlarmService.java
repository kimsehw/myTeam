package com.kimsehw.myteam.service.alarm;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.dto.alarm.AlarmDto;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlarmService {
    /**
     * 알람을 보냅니다.(저장)
     *
     * @param alarm 알람 객체
     */
    void send(Alarm alarm);

    /**
     * 해당 알람을 읽습니다.
     *
     * @param alarmId 알람 식별자
     */
    void read(Long alarmId);

    /**
     * 검색 조건에 맞는 알람들을 조회합니다.
     *
     * @param alarmSearchDto 검색 조건 Dto
     * @param memberId       대상 회원 아이디
     * @param pageable       페이지 정보
     * @return Page<AlarmDto>
     */
    Page<AlarmDto> getMyAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable);
}
