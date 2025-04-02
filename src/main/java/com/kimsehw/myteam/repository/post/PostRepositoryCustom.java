package com.kimsehw.myteam.repository.post;

import com.kimsehw.myteam.dto.post.PostDto;
import com.kimsehw.myteam.dto.post.PostSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostDto> findAllPostDto(PostSearchDto postSearchDto, Pageable pageable);
}
