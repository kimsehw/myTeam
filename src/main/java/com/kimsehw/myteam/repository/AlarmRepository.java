package com.kimsehw.myteam.repository;

import com.kimsehw.myteam.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
