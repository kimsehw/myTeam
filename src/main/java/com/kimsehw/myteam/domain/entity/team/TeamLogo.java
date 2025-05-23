package com.kimsehw.myteam.domain.entity.team;

import com.kimsehw.myteam.domain.entity.baseentity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_logo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamLogo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_logo_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "teamLogo")
    private Team team;

    private String imgName; // 파일명

    private String oriImgName; // 원본 이미지 파일명

    private String imgUrl; // 이미지 조회 경로

    private TeamLogo(Team team) {
        this.team = team;
        team.setTeamLogo(this);
    }

    public static TeamLogo of(Team team) {
        return new TeamLogo(team);
    }

    public void updateLogoImg(String oriImgName, String imgUrl, String imgName) {
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.imgName = imgName;
    }
}
