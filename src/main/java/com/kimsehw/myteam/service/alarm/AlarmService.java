package com.kimsehw.myteam.service.alarm;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.dto.alarm.AlarmDto;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlarmService<T extends Alarm> {
    /**
     * 알람을 보냅니다.(저장)
     *
     * @param alarm 알람 객체
     */
    Long send(T alarm);

    /**
     * 알람을 삭제 합니다.
     *
     * @param alarm 알람 객체
     */
    void delete(T alarm);

    /**
     * 해당 알람을 읽습니다.
     *
     * @param alarmId 알람 식별자
     */
    void read(Long alarmId);

    /**
     * 해당 알람을 조회합니다.
     *
     * @param alarmId 알람 아이디
     * @return Alarm
     * @throws EntityNotFoundException 응답 정보에 담긴 알람이 존재하지 않음
     */
    T getAlarm(Long alarmId);

    /**
     * 검색 조건에 맞는 알람들을 조회합니다.
     *
     * @param alarmSearchDto 검색 조건 Dto
     * @param memberId       대상 회원 아이디
     * @param pageable       페이지 정보
     * @return Page<AlarmDto>
     */
    Page<AlarmDto> getMyAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable);

    /**
     * 해당 알람에 대한 인가 검사를 실시합니다.
     *
     * @param alarm 알람
     * @param email 접근 회원 아이디
     */
    void validateAuthFrom(Alarm alarm, String email);

    void deleteOrHide(T alarm, String email);
}
