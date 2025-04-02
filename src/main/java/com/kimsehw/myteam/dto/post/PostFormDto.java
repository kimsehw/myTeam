package com.kimsehw.myteam.dto.post;

import com.kimsehw.myteam.constant.post.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class PostFormDto {

    @NotBlank(message = "제목을 입력해주세요.")
    @Length(max = 50, message = "제목은 50자 이하로 작성해주세요.")
    private String title;

    @Length(max = 500, message = "내용은 500자 이하로 작성해주세요.")
    private String detail;

    @NotNull(message = "글 유형을 선택해주세요.")
    private PostType postType;

    private Long teamId;
}
