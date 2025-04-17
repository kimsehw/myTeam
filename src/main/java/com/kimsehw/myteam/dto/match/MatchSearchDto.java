package com.kimsehw.myteam.dto.match;

import com.kimsehw.myteam.constant.search.SearchDateType;
import lombok.Data;

@Data
public class MatchSearchDto {

    private Boolean isDone;

    private String fromDate;

    private String toDate;

    private SearchDateType searchDateType;

    private Long teamId;
}
