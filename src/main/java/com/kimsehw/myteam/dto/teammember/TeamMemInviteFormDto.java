package com.kimsehw.myteam.dto.teammember;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kimsehw.myteam.constant.Position;
import lombok.Data;

@Data
public class TeamMemInviteFormDto {

    private String email;
    @JsonProperty("isNotUser") /* Boolean 사용하여 쉽게 해결도 가능 (@Data 롬복의 get 바인딩 문제 -> 내부적으로 Jackson 활용시)*/
    private boolean isNotUser;
    private Integer playerNum;
    private String name;
    private Position position;
}
