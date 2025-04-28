package com.kimsehw.myteam.service.alarm.basic;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.dto.alarm.AlarmDto;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import com.kimsehw.myteam.repository.alarm.basic.BasicAlarmRepository;
import com.kimsehw.myteam.service.alarm.AlarmService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log
public abstract class AbstractBasicAlarmServiceImpl<T extends Alarm> implements AlarmService<T> {

    public static final String NO_ALARM_ERROR = "존재하지 않는 알람에 접근하였습니다. 알람 정보를 확인해주세요.";
    public static final String NO_AUTH_ALARM_ACCESS = "접근 권한이 없는 알람에 접근하였습니다.";
    public static final String CANT_DELETE_MY_ALARM = "상대방이 확인한 알람은 삭제할 수 없습니다.";

    private final BasicAlarmRepository<T> repository;

    @Transactional
    @Override
    public Long send(T alarm) {
        return repository.save(alarm).getId();
    }

    @Transactional
    @Override
    public void delete(T alarm) {
        repository.delete(alarm);
    }

    @Transactional
    @Override
    public void read(Long alarmId, String email) {
        T alarm = getAlarm(alarmId);
        validateAuthFrom(alarm, email);
        alarm.read(email);
    }

    @Override
    public Page<AlarmDto> getMyAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable) {
        return searchAlarms(alarmSearchDto, memberId, pageable).map(alarm -> AlarmDto.from(alarm, memberId));
    }

    public abstract Page<T> searchAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable);

    @Override
    public T getAlarm(Long alarmId) {
        return repository.findById(alarmId)
                .orElseThrow(() -> new EntityNotFoundException(NO_ALARM_ERROR));
    }

    @Override
    public void validateAuthFrom(Alarm alarm, String email) {
        if (alarm.checkAuth(email)) {
            return;
        }
        throw new IllegalArgumentException(NO_AUTH_ALARM_ACCESS);
    }

    @Transactional
    @Override
    public void deleteOrHide(T alarm, String email) {
        if (alarm.deleteOrHide(email)) {
            delete(alarm);
        }
        log.info("basicAlarmDelete");
    }
}
