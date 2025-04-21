package com.kimsehw.myteam.repository.alarm;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository<T extends Alarm> extends JpaRepository<T, Long> {
    List<Long> findToMemberIdsByFromMemberIdAndFromTeamId(Long fromMemId, Long teamId);
}
