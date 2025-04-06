package com.kimsehw.myteam.domain.entity.match;

import com.kimsehw.myteam.domain.embedded.record.MatchPersonalRecord;
import com.kimsehw.myteam.domain.entity.TeamMember;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchMemberRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matchMemRecord_id")
    private Long id;

    private String detail;

    private MatchPersonalRecord matchPersonalRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamMem_id")
    private TeamMember teamMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;
}
