package com.kimsehw.myteam.dto.match;

import java.util.Map;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class MatchListResponse {
    private Map<Long, String> myTeamIdsAndNames;
    private Page<MatchDto> matches;

    public MatchListResponse(Map<Long, String> myTeamIdsAndNames, Page<MatchDto> matches) {
        this.myTeamIdsAndNames = myTeamIdsAndNames;
        this.matches = matches;
    }
}
