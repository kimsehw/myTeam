package com.kimsehw.myteam.repository.teammember;

import com.kimsehw.myteam.dto.teammember.TeamMemberDto;
import com.kimsehw.myteam.entity.TeamMember;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>, TeamMemberRepositoryCustom {

    @Query("select tm from TeamMember tm"
            + " where tm.member.id = :memberId")
    List<TeamMember> findAllByMemberId(Long memberId);

    @Query("select new com.kimsehw.myteam.dto.teammember.TeamMemberDto(tm.id, tm.teamRole, tm.playerNum,"
            + " tm.name, tm.teamMemberRecord, tm.attendance)"
            + " from TeamMember tm"
            + " where tm.team.id = :teamId")
    Page<TeamMemberDto> findAllTeamMemberDtoByTeamId(@Param("teamId") Long teamId, Pageable pageable);

    TeamMember findByPlayerNumAndTeamId(int playerNum, Long teamId);

    @Query("select tm from TeamMember tm"
            + " where tm.member.id = :memberId"
            + " and tm.team.id = :teamId")
    TeamMember findByMemberIdAndTeamId(Long memberId, Long teamId);
}
