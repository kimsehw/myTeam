package com.kimsehw.myteam.embedded.record;

import com.kimsehw.myteam.constant.Position;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Embeddable
@Getter
public class MatchPersonalRecord extends BaseRecord {

    @Enumerated(EnumType.STRING)
    private Position position;
}
