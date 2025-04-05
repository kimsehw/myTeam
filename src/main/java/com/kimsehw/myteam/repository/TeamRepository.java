package com.kimsehw.myteam.repository;

import com.kimsehw.myteam.domain.entity.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("select t from Team t"
            + " where t.member.id = :memberId"
            + " and t.teamName = :teamName")
    Team findByMemberIdAndTeamName(Long memberId, String teamName);

    Team findByMemberId(Long memberId);

    /*@Query("select new com.kimsehw.myteam.dto.team.TeamInfoDto(t.id, t.teamName, t.teamName,"
            + " (select count(tm) from TeamMember tm where tm.team.id = :teamId),"
            + " t.region, t.ageRange, t.teamRecord, t.teamDetail)"
            + " from Team t"
            + " where t.id = :teamId"
    )
    TeamInfoDto getTeamInfoDtoById(Long teamId);*/

    @Query("select t from Team t"
            + " join fetch t.teamLogo tl"
            + " where t.id = :teamId"
    )
    Team findWithTeamMembersAndTeamLogoById(Long teamId);
}
