package com.kimsehw.myteam.entity;

import com.kimsehw.myteam.constant.Role;
import com.kimsehw.myteam.dto.member.MemberFormDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member {

    @Id
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<Team> teams = new ArrayList<>();

    private Member(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        name = memberFormDto.getName();
        email = memberFormDto.getEmail();
        password = passwordEncoder.encode(memberFormDto.getPassword());
        role = Role.MEMBER;
    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        return new Member(memberFormDto, passwordEncoder);
    }

    public void addTeam(Team team) {
        teams.add(team);
        if (role == Role.MEMBER) {
            role = Role.LEADER;
        }
    }
}
