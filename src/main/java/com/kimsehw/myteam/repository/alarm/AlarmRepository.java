package com.kimsehw.myteam.repository.alarm;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.repository.alarm.basic.BasicAlarmRepository;

public interface AlarmRepository extends BasicAlarmRepository<Alarm>, AlarmRepositoryCustom {
}
