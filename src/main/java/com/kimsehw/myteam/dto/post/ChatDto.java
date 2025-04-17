package com.kimsehw.myteam.dto.post;

import com.kimsehw.myteam.domain.entity.post.Chat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Data;

@Data
public class ChatDto {
    private Long id;
    private String createdBy;
    private String regTime;
    private String detail;
    private List<ChatDto> childChats;

    public ChatDto(Long id, String createdBy, LocalDateTime regTime, String detail) {
        this.id = id;
        this.detail = detail;
        this.createdBy = createdBy;
        this.regTime = regTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"));
    }

    public ChatDto(Long id, String createdBy, LocalDateTime regTime, String detail, List<ChatDto> childChats) {
        this.id = id;
        this.createdBy = createdBy;
        this.detail = detail;
        this.regTime = regTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm"));
        this.childChats = childChats;
    }

    public static ChatDto createChildChatDto(Chat childChat) {
        return new ChatDto(childChat.getId(), childChat.getCreatedBy(), childChat.getRegTime(), childChat.getDetail());
    }

    public static ChatDto createChatDto(Chat chat, List<ChatDto> childChatDtos) {
        return new ChatDto(chat.getId(), chat.getCreatedBy(), chat.getRegTime(), chat.getDetail(), childChatDtos);
    }
}