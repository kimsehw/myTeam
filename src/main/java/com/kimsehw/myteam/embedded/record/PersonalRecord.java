package com.kimsehw.myteam.embedded.record;

import com.kimsehw.myteam.constant.Position;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class PersonalRecord extends BaseRecord {


    private int assist = 0;

    private String winRate;

    @Enumerated(EnumType.STRING)
    private Position position;

    public PersonalRecord(Position position) {
        this.winRate = this.calcWinRate(getWins(), getLoses(), getDraws());
        this.position = position;
    }

    public void changePosition(Position position) {
        this.position = position;
    }
}
