package com.kimsehw.myteam.dto.match;

import lombok.Data;

@Data
public class MatchInviteFormDto {

    public Boolean isNotUser;
    public Long inviteeTeamId;
    public String inviteeEmail;
    public String inviteeTeamName;
    public String matchDate;
    public Integer matchTime;
}
