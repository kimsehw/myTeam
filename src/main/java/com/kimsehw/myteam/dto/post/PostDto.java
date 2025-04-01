package com.kimsehw.myteam.dto.post;

import com.kimsehw.myteam.constant.post.PostType;
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

    public PostDto(Long id, LocalDateTime regTime, String title, PostType postType, String createdBy) {
        this.id = id;
        this.regTime = regTime.format(DateTimeFormatter.ofPattern("yyyy-M-dd"));
        this.title = title;
        this.postType = postType;
        this.createdBy = createdBy;
    }
}
