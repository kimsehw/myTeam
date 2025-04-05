package com.kimsehw.myteam.service;

import com.kimsehw.myteam.dto.post.ChatFormDto;
import com.kimsehw.myteam.entity.post.Chat;
import com.kimsehw.myteam.repository.ChatRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log
public class ChatService {

    private final ChatRepository chatRepository;

    public List<Chat> getChildChatsOf(Long parentId) {
        Chat chat = chatRepository.findAllWithChildChatByIdUseFetch(parentId);
        return chat.getSortingChildChats();
    }

    @Transactional
    public Long addChildChat(ChatFormDto chatFormDto) {
        Chat parentChat = chatRepository.findById(chatFormDto.getParentId()).orElseThrow(EntityNotFoundException::new);
        Chat childChat = Chat.createChildChat(parentChat, chatFormDto.getContent());
        return childChat.getId();
    }
}
