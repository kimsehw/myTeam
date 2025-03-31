package com.kimsehw.myteam.dto.post;

import com.kimsehw.myteam.constant.PostType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PostDto {

    private Long id;
    private LocalDateTime regTime;
    private String title;
    private PostType postType;
    private String createdBy;

    public PostDto(Long id, LocalDateTime regTime, String title, PostType postType, String createdBy) {
        this.id = id;
        this.regTime = regTime;
        this.title = title;
        this.postType = postType;
        this.createdBy = createdBy;
    }
}
