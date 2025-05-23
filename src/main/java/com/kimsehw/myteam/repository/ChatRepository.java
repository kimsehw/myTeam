package com.kimsehw.myteam.repository;

import com.kimsehw.myteam.domain.entity.post.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("select c from Chat c"
            + " left join fetch c.childChats cs"
            + " where c.id = :parentId")
    Chat findAllWithChildChatByIdUseFetch(Long parentId);
}
