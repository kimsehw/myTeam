package com.kimsehw.myteam.entity.post;

import com.kimsehw.myteam.constant.post.PostType;
import com.kimsehw.myteam.dto.post.PostFormDto;
import com.kimsehw.myteam.entity.baseentity.BaseEntity;
import com.kimsehw.myteam.entity.team.Team;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    private String detail;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Chat> chats = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private Post(PostFormDto postFormDto, Team team) {
        title = postFormDto.getTitle();
        detail = postFormDto.getDetail();
        postType = postFormDto.getPostType();
        this.team = team;
    }

    public static Post newPost(PostFormDto postFormDto, Team team) {
        return new Post(postFormDto, team);
    }

    public List<Chat> getSortingChats() {
        return chats.stream()
                .sorted(Comparator.comparing(Chat::getRegTime).reversed())
                .toList();
    }

    public void addChat(Chat chat) {
        chats.add(chat);
    }

    public void update(PostFormDto postFormDto, Team team) {
        detail = postFormDto.getDetail();
        title = postFormDto.getTitle();
        postType = postFormDto.getPostType();
        this.team = team;
    }
}
