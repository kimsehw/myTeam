package com.kimsehw.myteam.service;

import com.kimsehw.myteam.entity.post.Chat;
import com.kimsehw.myteam.repository.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public List<Chat> getChildChatsOf(Long parentId) {
        Chat chat = chatRepository.findAllWithChildChatByIdUseFetch(parentId);
        List<Chat> childChats = chat.getSoringChildChats();
        return childChats;
    }
}
