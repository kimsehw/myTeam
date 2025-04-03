package com.kimsehw.myteam.dto.post;

import com.kimsehw.myteam.constant.team.AgeRange;
import com.kimsehw.myteam.constant.team.Region;
import lombok.Data;

@Data
public class PostDetailDto {

    private Long id;
    private String title;
    private String detail;
    private String createdBy;
    private Long teamId;
    private String logoUrl;
    private String teamName;
    private Region region;
    private AgeRange ageRange;

    public PostDetailDto(Long id, String title, String detail, String createdBy, Long teamId, String logoUrl,
                         String teamName, Region region, AgeRange ageRange) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.createdBy = createdBy;
        this.teamId = teamId;
        this.logoUrl = logoUrl;
        this.teamName = teamName;
        this.region = region;
        this.ageRange = ageRange;
    }
}
