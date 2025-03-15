package com.kimsehw.myteam.embedded.record;

import com.kimsehw.myteam.constant.Position;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class PersonalRecord extends BaseRecord implements RecordCalculator {

    private String winRate;

    @Enumerated(EnumType.STRING)
    private Position position;
}
