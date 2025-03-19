package com.kimsehw.myteam.embedded.record;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class TeamRecord extends BaseRecord {

    private String winRate;

    public TeamRecord() {
        super();
        winRate = this.calcWinRate(super.getWins(), super.getLoses(), super.getDraws());
    }
}
