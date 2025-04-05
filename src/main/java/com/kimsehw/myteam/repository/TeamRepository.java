package com.kimsehw.myteam.repository;

import com.kimsehw.myteam.domain.entity.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("select t from Team t"
            + " join fetch t.leader l"
            + " where l.member.id = :memberId"
            + " and t.teamName = :teamName")
    Team findByMemberIdAndTeamName(Long memberId, String teamName);

    @Query("select t from Team t"
            + " join fetch t.teamLogo tl"
            + " where t.id = :teamId"
    )
    Team findWithTeamMembersAndTeamLogoById(Long teamId);
}
