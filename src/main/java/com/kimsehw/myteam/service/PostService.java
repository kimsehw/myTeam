package com.kimsehw.myteam.service;

import com.kimsehw.myteam.dto.post.PostDetailDto;
import com.kimsehw.myteam.dto.post.PostDto;
import com.kimsehw.myteam.dto.post.PostFormDto;
import com.kimsehw.myteam.dto.post.PostSearchDto;
import com.kimsehw.myteam.entity.post.Chat;
import com.kimsehw.myteam.entity.post.Post;
import com.kimsehw.myteam.entity.team.Team;
import com.kimsehw.myteam.repository.post.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Log
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

    public PostDetailDto getPostDetail(Long postId) {
        return postRepository.findPostDetailDtoById(postId);
    }

    public List<Chat> getChats(Long postId) {
        Post post = postRepository.findAllWithChatAndChildChatsByIdUseFetch(postId);
        List<Chat> chats = post.getSortingChats();
//        log.info("postService.getChats2() chats size : " + chats.size());
        return chats;
    }

    @Transactional
    public Long addChat(Long postId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);
        Chat chat = Chat.addChatIn(post, content);
        return chat.getId();
    }

    public Post findById(Long postId) {
        return postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);
    }
}
