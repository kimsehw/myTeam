package com.kimsehw.myteam.service.alarm.basic;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.dto.alarm.AlarmDto;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import com.kimsehw.myteam.repository.alarm.basic.BasicAlarmRepository;
import com.kimsehw.myteam.service.alarm.AlarmService;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
public abstract class AbstractBasicAlarmServiceImpl<T extends Alarm> implements AlarmService<T> {

    public static final String NO_ALARM_ERROR = "존재하지 않는 알람에 접근하였습니다. 알람 정보를 확인해주세요.";

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
    public void read(Long alarmId) {
        Optional<T> alarm = repository.findById(alarmId);
        alarm.ifPresent(Alarm::read);
    }

    @Override
    public Page<AlarmDto> getMyAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable) {
        return searchAlarms(alarmSearchDto, memberId, pageable).map(alarm -> {
            if (memberId.equals(alarm.getFromMember().getId())) {
                return AlarmDto.ofSent(alarm);
            }
            return AlarmDto.ofReceive(alarm);
        });
    }

    public abstract Page<T> searchAlarms(AlarmSearchDto alarmSearchDto, Long memberId, Pageable pageable);

    @Override
    public T getAlarm(Long alarmId) {
        return repository.findById(alarmId)
                .orElseThrow(() -> new EntityNotFoundException(NO_ALARM_ERROR));
    }
}
