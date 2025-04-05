package com.kimsehw.myteam.dto.post;

import com.kimsehw.myteam.constant.team.AgeRange;
import com.kimsehw.myteam.constant.team.Region;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class PostDetailDto {

    private Long id;
    private String title;
    private String detail;
    private String createdBy;
    private String regTime;
    private Long teamId;
    private String logoUrl;
    private String teamName;
    private Region region;
    private AgeRange ageRange;

    public PostDetailDto(Long id, String title, String detail, String createdBy, LocalDateTime regTime, Long teamId,
                         String logoUrl,
                         String teamName, Region region, AgeRange ageRange) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.createdBy = createdBy;
        this.regTime = regTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"));
        this.teamId = teamId;
        this.logoUrl = logoUrl;
        this.teamName = teamName;
        this.region = region;
        this.ageRange = ageRange;
    }
}
