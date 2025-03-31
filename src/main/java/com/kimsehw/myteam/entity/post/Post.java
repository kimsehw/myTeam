package com.kimsehw.myteam.entity.post;

import com.kimsehw.myteam.constant.PostType;
import com.kimsehw.myteam.dto.post.PostFormDto;
import com.kimsehw.myteam.entity.baseentity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    private List<Chat> chats;

    private Post(PostFormDto postFormDto) {
        title = postFormDto.getTitle();
        detail = postFormDto.getDetail();
        postType = postFormDto.getPostType();
    }

    public static Post newPost(PostFormDto postFormDto) {
        return new Post(postFormDto);
    }
}
