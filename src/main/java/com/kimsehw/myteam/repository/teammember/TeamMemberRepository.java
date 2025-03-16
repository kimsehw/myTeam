package com.kimsehw.myteam.repository.teammember;

import com.kimsehw.myteam.entity.TeamMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>, TeamMemberRepositoryCustom {

    @Query("select tm from TeamMember tm"
            + " where tm.member.id = :memberId")
    List<TeamMember> findByMemberId(Long memberId);
}
