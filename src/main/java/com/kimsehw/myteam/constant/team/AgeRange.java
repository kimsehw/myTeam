package com.kimsehw.myteam.constant.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AgeRange {
    TWENTY("20대"), THIRTY("30대"), FORTY("40대"), FIFTY("50대"), SIXTY("60대");

    private String range;
}
