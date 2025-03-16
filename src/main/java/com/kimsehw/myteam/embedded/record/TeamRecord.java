package com.kimsehw.myteam.embedded.record;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class TeamRecord extends BaseRecord implements RecordCalculator {

    private String winRate;
}
