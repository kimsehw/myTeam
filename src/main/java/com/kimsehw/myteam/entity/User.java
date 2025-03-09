package com.kimsehw.myteam.entity;

import com.kimsehw.myteam.dto.UserFormDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToMany(mappedBy = "user")
    private List<Team> teams = new ArrayList<>();

    private User(UserFormDto userFormDto) {
        name = userFormDto.getName();
        email = userFormDto.getEmail();
        password = userFormDto.getPassword();
    }

    public static User createMember(UserFormDto userFormDto) {
        return new User(userFormDto);
    }
}
