package com.kimsehw.myteam.domain.embedded.record;

import com.kimsehw.myteam.constant.Position;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Embeddable
public class MatchPersonalRecord extends BaseRecord {

    private int assist = 0;

    @Enumerated(EnumType.STRING)
    private Position position;

    public MatchPersonalRecord(Position position) {
        this.position = position;
    }
}
