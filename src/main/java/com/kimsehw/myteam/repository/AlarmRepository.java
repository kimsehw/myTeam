package com.kimsehw.myteam.repository;

import com.kimsehw.myteam.constant.AlarmType;
import com.kimsehw.myteam.entity.Alarm;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("select al.toMember.id from Alarm al"
            + " where al.fromMember.id = :memberId"
            + " and al.fromTeamId = :teamId"
            + " and al.alarmType = :alarmType")
    List<Long> findAllToMemberIdByFromMemberIdAndFromTeamId(Long memberId, Long teamId, AlarmType alarmType);
}
