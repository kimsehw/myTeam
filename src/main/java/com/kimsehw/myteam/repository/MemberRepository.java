package com.kimsehw.myteam.repository;

import com.kimsehw.myteam.dto.member.MyTeamsInfoDto;
import com.kimsehw.myteam.entity.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query("select new com.kimsehw.myteam.dto.member.MyTeamsInfoDto(t.id, t.teamName)"
            + " from Member m"
            + " join m.teams t"
            + " where m.email = :email")
    List<MyTeamsInfoDto> findMyTeamsInfoByEmail(String email);
}
