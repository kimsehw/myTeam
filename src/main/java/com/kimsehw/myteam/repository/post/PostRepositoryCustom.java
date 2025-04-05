package com.kimsehw.myteam.repository.post;

import com.kimsehw.myteam.dto.post.PostDetailDto;
import com.kimsehw.myteam.dto.post.PostDto;
import com.kimsehw.myteam.dto.post.PostSearchDto;
import com.kimsehw.myteam.domain.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostDto> findAllPostDto(PostSearchDto postSearchDto, Pageable pageable);

    PostDetailDto findPostDetailDtoById(Long postId);

    Post findAllWithChatAndChildChatsByIdUseFetch(Long postId);
}
