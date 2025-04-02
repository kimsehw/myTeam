package com.kimsehw.myteam.service;

import com.kimsehw.myteam.dto.post.PostDto;
import com.kimsehw.myteam.dto.post.PostFormDto;
import com.kimsehw.myteam.dto.post.PostSearchDto;
import com.kimsehw.myteam.entity.post.Post;
import com.kimsehw.myteam.entity.team.Team;
import com.kimsehw.myteam.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void savePost(PostFormDto postFormDto, Team team) {
        Post post = Post.newPost(postFormDto, team);
        postRepository.save(post);
    }

    public Page<PostDto> getPosts(PostSearchDto postSearchDto, Pageable pageable) {
        return postRepository.findAllPostDto(postSearchDto, pageable);
    }
}
