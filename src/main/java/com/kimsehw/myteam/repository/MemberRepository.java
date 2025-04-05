package com.kimsehw.myteam.repository;

import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.dto.member.MyTeamsInfoDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query("select new com.kimsehw.myteam.dto.member.MyTeamsInfoDto(t.id, t.teamName)"
            + " from Member m"
            + " join m.myTeams ts"
            + " join ts.team t"
            + " where m.email = :email")
    List<MyTeamsInfoDto> findMyTeamsInfoByEmail(String email);
}
