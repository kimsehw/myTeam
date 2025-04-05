package com.kimsehw.myteam.domain.embedded.record;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public class BaseRecord implements RecordCalculator {

    private int goals = 0;
    private int wins = 0;
    private int loses = 0;
    private int draws = 0;

}
