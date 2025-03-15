package com.kimsehw.myteam.embedded.record;

import jakarta.persistence.Embeddable;

@Embeddable
public class TeamRecord extends BaseRecord implements RecordCalculator {

    private String winRate;
}
