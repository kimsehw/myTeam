package com.kimsehw.myteam.repository.member;

import com.kimsehw.myteam.domain.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByEmail(String email);

    @Query("select m"
            + " from Member m"
            + " join fetch m.myTeams ts"
            + " join fetch ts.team t"
            + " where m.email = :email")
    Optional<Member> findWithMyTeamsInfoByEmail(String email);
}
