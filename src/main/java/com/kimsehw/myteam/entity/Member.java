package com.kimsehw.myteam.entity;

import com.kimsehw.myteam.constant.member.Role;
import com.kimsehw.myteam.dto.member.MemberFormDto;
import com.kimsehw.myteam.embedded.record.PersonalRecord;
import com.kimsehw.myteam.entity.baseentity.BaseTimeEntity;
import com.kimsehw.myteam.entity.team.Team;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Embedded
    private PersonalRecord memberRecord;

    @OneToMany(mappedBy = "member")
    private List<Team> teams = new ArrayList<>();

    private Member(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        name = memberFormDto.getName();
        email = memberFormDto.getEmail();
        password = passwordEncoder.encode(memberFormDto.getPassword());
        role = Role.MEMBER;
        memberRecord = new PersonalRecord(memberFormDto.getPosition());
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
