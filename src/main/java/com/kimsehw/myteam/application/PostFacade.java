package com.kimsehw.myteam.application;

import com.kimsehw.myteam.dto.member.MyTeamsInfoDto;
import com.kimsehw.myteam.dto.post.ChatDto;
import com.kimsehw.myteam.dto.post.PostDetailDto;
import com.kimsehw.myteam.dto.post.PostDto;
import com.kimsehw.myteam.dto.post.PostFormDto;
import com.kimsehw.myteam.dto.post.PostSearchDto;
import com.kimsehw.myteam.entity.post.Chat;
import com.kimsehw.myteam.entity.team.Team;
import com.kimsehw.myteam.service.ChatService;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.PostService;
import com.kimsehw.myteam.service.TeamService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;
    private final MemberService memberService;
    private final TeamService teamService;
    private final ChatService chatService;


    public List<MyTeamsInfoDto> findMyTeamsInfoByEmail(String email) {
        return memberService.findMyTeamsInfoByEmail(email);
    }

    public void savePost(PostFormDto postFormDto) {
        Team team = null;
        Long teamId = postFormDto.getTeamId();
        if (teamId != null) {
            team = teamService.findById(postFormDto.getTeamId());
        }

        postService.savePost(postFormDto, team);
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
        List<Chat> childChats = chatService.getChildChatsOf(parentChat.getId());
        List<ChatDto> childChatDtos = childChats.stream().map(ChatDto::createChildChatDto).toList();
        return ChatDto.createChatDto(parentChat, childChatDtos);
    }
}
