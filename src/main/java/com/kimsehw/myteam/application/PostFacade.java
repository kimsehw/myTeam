package com.kimsehw.myteam.application;

import com.kimsehw.myteam.domain.FieldError;
import com.kimsehw.myteam.dto.member.MyTeamsInfoDto;
import com.kimsehw.myteam.dto.post.ChatDto;
import com.kimsehw.myteam.dto.post.ChatFormDto;
import com.kimsehw.myteam.dto.post.PostDetailDto;
import com.kimsehw.myteam.dto.post.PostDto;
import com.kimsehw.myteam.dto.post.PostFormDto;
import com.kimsehw.myteam.dto.post.PostSearchDto;
import com.kimsehw.myteam.domain.entity.post.Chat;
import com.kimsehw.myteam.domain.entity.post.Post;
import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.exception.FieldErrorException;
import com.kimsehw.myteam.service.ChatService;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.PostService;
import com.kimsehw.myteam.service.TeamService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log
public class PostFacade {

    public static final String BLANK_CHAT_ERROR = "댓글에 빈칸은 입력할 수 없습니다.";
    private final PostService postService;
    private final MemberService memberService;
    private final TeamService teamService;
    private final ChatService chatService;


    public List<MyTeamsInfoDto> findMyTeamsInfoByEmail(String email) {
        return memberService.findMyTeamsInfoByEmail(email);
    }

    public void savePost(PostFormDto postFormDto) {
        Team team = getTeam(postFormDto);

        postService.savePost(postFormDto, team);
    }

    private Team getTeam(PostFormDto postFormDto) {
        Team team = null;
        Long teamId = postFormDto.getTeamId();
        if (teamId != null) {
            team = teamService.findById(postFormDto.getTeamId());
        }
        return team;
    }

    public Page<PostDto> getPosts(PostSearchDto postSearchDto, Pageable pageable) {
        return postService.getPosts(postSearchDto, pageable);
    }

    public PostDetailDto getPostDetail(Long postId) {
        return postService.getPostDetail(postId);
    }

    public List<ChatDto> getChats(Long postId) {
        List<Chat> parentChats = postService.getChats(postId);
        return parentChats.stream()
                .map(this::getChatDtoOf)
                .toList();
    }

    private ChatDto getChatDtoOf(Chat parentChat) {
//        log.info("postService.getChats2() start");
        List<Chat> childChats = parentChat.getSortingChildChats();
        List<ChatDto> childChatDtos = childChats.stream().map(ChatDto::createChildChatDto).toList();
//        log.info("postService.getChats2() childChats size : " + childChats.size());
        return ChatDto.createChatDto(parentChat, childChatDtos);
    }

    public Long addChat(Long postId, ChatFormDto chatFormDto) {
        if (chatFormDto.getContent().isBlank()) {
            throw new FieldErrorException(FieldError.of("content", BLANK_CHAT_ERROR));
        }
        if (chatFormDto.getParentId() == null) {
            return postService.addChat(postId, chatFormDto.getContent());
        }
        return chatService.addChildChat(chatFormDto);
    }

    public boolean isWriter(Long postId, Principal principal) {
        if (principal == null) {
            return false;
        }
        String email = principal.getName();
        Post post = postService.findById(postId);
        return post.getCreatedBy().equals(email);
    }

    public void deletePost(Long postId) {
        postService.delete(postId);
    }

    public PostFormDto getPostFormOf(Long postId) {
        return postService.getPostFormOf(postId);
    }

    public void updatePost(Long postId, PostFormDto postFormDto) {
        Team team = getTeam(postFormDto);
        postService.update(postId, postFormDto, team);
    }
}
