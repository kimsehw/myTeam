package com.kimsehw.myteam.repository;

import com.kimsehw.myteam.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("select t from Team t"
            + " where t.member.id = :memberId"
            + " and t.teamName = :teamName")
    Team findByMemberIdAndTeamName(Long memberId, String teamName);

    Team findByMemberId(Long memberId);
}
