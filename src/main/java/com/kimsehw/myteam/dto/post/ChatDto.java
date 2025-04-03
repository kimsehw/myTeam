package com.kimsehw.myteam.dto.post;

import com.kimsehw.myteam.entity.post.Chat;
import java.util.List;
import lombok.Data;

@Data
public class ChatDto {
    private Long id;
    private String detail;
    private List<ChatDto> childChats;

    public ChatDto(Long id, String detail) {
        this.id = id;
        this.detail = detail;
    }

    public ChatDto(Long id, String detail, List<ChatDto> childChats) {
        this.id = id;
        this.detail = detail;
        this.childChats = childChats;
    }

    public static ChatDto createChildChatDto(Chat childChat) {
        return new ChatDto(childChat.getId(), childChat.getDetail());
    }

    public static ChatDto createChatDto(Chat chat, List<ChatDto> childChatDtos) {
        return new ChatDto(chat.getId(), chat.getDetail(), childChatDtos);
    }
}