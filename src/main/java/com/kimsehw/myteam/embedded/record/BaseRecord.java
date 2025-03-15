package com.kimsehw.myteam.embedded.record;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class BaseRecord {

    private int goals = 0;
    private int wins = 0;
    private int loses = 0;

}
