package com.kimsehw.myteam.constant.team;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Region {
    SEOUL("서울"), GYEONGGIDO("경기"), INCHEON("인천");

    private String regionName;
}
