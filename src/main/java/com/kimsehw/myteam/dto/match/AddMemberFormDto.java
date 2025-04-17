package com.kimsehw.myteam.dto.match;

import java.util.List;
import lombok.Data;

@Data
public class AddMemberFormDto {

    private List<Long> addMemberIds;
    private Long matchId;
}
