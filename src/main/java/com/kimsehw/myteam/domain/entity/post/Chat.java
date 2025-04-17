package com.kimsehw.myteam.domain.entity.post;

import com.kimsehw.myteam.domain.entity.baseentity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    private String detail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentChat_id")
    private Chat parentChat;

    @OneToMany(mappedBy = "parentChat", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 25)
    private List<Chat> childChats = new ArrayList<>();

    public List<Chat> getSortingChildChats() {
        return childChats.stream()
                .sorted(Comparator.comparing(Chat::getRegTime).reversed())
                .toList();
    }

    private Chat(String detail, Post post) {
        this.detail = detail;
        this.post = post;
        post.addChat(this);
    }

    public static Chat addChatIn(Post post, String content) {
        return new Chat(content, post);
    }

    public Chat(String detail, Chat parentChat) {
        this.detail = detail;
        this.parentChat = parentChat;
        parentChat.addChildChat(this);
    }

    private void addChildChat(Chat childChat) {
        childChats.add(childChat);
    }

    public static Chat createChildChat(Chat parentChat, String content) {
        return new Chat(content, parentChat);
    }
}
