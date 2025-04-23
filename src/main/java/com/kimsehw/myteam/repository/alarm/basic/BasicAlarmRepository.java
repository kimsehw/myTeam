package com.kimsehw.myteam.repository.alarm.basic;

import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasicAlarmRepository<T extends Alarm> extends JpaRepository<T, Long>, BasicAlarmRepositoryCustom<T> {
    @EntityGraph(attributePaths = {"fromMember", "toMember", "fromTeam", "toTeam"})
    Page<T> findAllByFromMemberIdOrToMemberIdOrderByRegTime(Long fromMemId, Long toMemId, Pageable pageable);
}
