package com.kimsehw.myteam.repository;

import com.kimsehw.myteam.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
}
