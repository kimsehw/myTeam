package com.kimsehw.myteam.dto.post;

import com.kimsehw.myteam.constant.post.PostType;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class PostDto {

    private Long id;
    private String regTime;
    private String title;
    private PostType postType;
    private String createdBy;
    private String teamName;

    @QueryProjection
    public PostDto(Long id, LocalDateTime regTime, String title, PostType postType, String createdBy, String teamName) {
        this.id = id;
        this.regTime = regTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.title = title;
        this.postType = postType;
        this.createdBy = createdBy;
        this.teamName = teamName;
    }
}
