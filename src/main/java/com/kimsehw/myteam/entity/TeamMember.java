package com.kimsehw.myteam.entity;

import com.kimsehw.myteam.constant.TeamRole;
import com.kimsehw.myteam.embedded.record.PersonalRecord;
import com.kimsehw.myteam.entity.baseentity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class TeamMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teamMember_id")
    private Long id;

    private String detail;

    @Enumerated
    private TeamRole teamRole;

    @Embedded
    private PersonalRecord teamMemberRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId")
    private Team team;

}
