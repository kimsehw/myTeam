package com.kimsehw.myteam.entity.post;

import com.kimsehw.myteam.entity.baseentity.BaseEntity;
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
    private List<Chat> childChats = new ArrayList<>();

    public List<Chat> getSoringChildChats() {
        return childChats.stream()
                .sorted(Comparator.comparing(Chat::getRegTime).reversed())
                .toList();
    }
}
