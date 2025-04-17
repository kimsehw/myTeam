package com.kimsehw.myteam.dto.post;

import com.kimsehw.myteam.constant.post.PostType;
import com.kimsehw.myteam.domain.entity.post.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class PostFormDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Length(max = 50, message = "제목은 50자 이하로 작성해주세요.")
    private String title;

    @NotBlank(message = "내용은 빈칸일 수 없습니다. 내용을 입력해주세요.")
    @Length(max = 500, message = "내용은 500자 이하로 작성해주세요.")
    private String detail;

    @NotNull(message = "글 유형을 선택해주세요.")
    private PostType postType;

    private Long teamId;

    public PostFormDto(String title, String detail, PostType postType, Long teamId) {
        this.title = title;
        this.detail = detail;
        this.postType = postType;
        this.teamId = teamId;
    }

    public static PostFormDto of(Post post) {
        return new PostFormDto(post.getTitle(), post.getDetail(), post.getPostType(), post.getTeam().getId());
    }
}
