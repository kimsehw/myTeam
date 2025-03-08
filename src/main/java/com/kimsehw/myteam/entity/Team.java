package com.kimsehw.myteam.entity;

import com.kimsehw.myteam.constant.AgeRange;
import com.kimsehw.myteam.constant.Region;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @NotBlank(message = "팀명은 필수 입력 값 입니다.")
    private String teamName;

    @NotNull(message = "활동 지역은 필수 입력 값 입니다.")
    @Enumerated(EnumType.STRING)
    private Region region;

    @NotNull(message = "나이대를 최소 하나 설정해주세요.")
    @Enumerated(EnumType.STRING)
    private AgeRange ageRange;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
