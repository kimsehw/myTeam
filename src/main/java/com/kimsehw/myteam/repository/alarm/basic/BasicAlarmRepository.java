package com.kimsehw.myteam.repository.alarm.basic;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicAlarmRepository<T extends Alarm> extends JpaRepository<T, Long> {
}
