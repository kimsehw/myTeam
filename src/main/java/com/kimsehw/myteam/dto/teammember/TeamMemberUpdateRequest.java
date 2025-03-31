package com.kimsehw.myteam.dto.teammember;

import java.util.List;
import lombok.Data;

@Data
public class TeamMemberUpdateRequest {
    private Long teamId;
    private List<TeamMemberUpdateDto> TeamMemberUpdateDtos;
}
