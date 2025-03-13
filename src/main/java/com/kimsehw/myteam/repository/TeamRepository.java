package com.kimsehw.myteam.repository;

import com.kimsehw.myteam.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
