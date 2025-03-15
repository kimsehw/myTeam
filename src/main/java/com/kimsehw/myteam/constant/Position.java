package com.kimsehw.myteam.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Position {

    FW("공격수"), MF("미드필더"), DF("수비수"), CUSTOM("");

    private String positionDetails;
}
