package com.kimsehw.myteam.entity;

import com.kimsehw.myteam.constant.Role;
import com.kimsehw.myteam.dto.MemberFormDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Team> teams = new ArrayList<>();

    private Member(MemberFormDto memberFormDto) {
        name = memberFormDto.getName();
        email = memberFormDto.getEmail();
        password = memberFormDto.getPassword();
        role = Role.MEMBER;
    }

    public static Member createMember(MemberFormDto memberFormDto) {
        return new Member(memberFormDto);
    }
}
