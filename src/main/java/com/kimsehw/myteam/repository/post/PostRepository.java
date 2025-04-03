package com.kimsehw.myteam.repository.post;

import com.kimsehw.myteam.dto.post.PostDto;
import com.kimsehw.myteam.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("select new com.kimsehw.myteam.dto.post.PostDto(p.id, p.regTime, p.title, p.postType, p.createdBy, t.teamName)"
            + " from Post p"
            + " left join p.team t"
            + " order by p.regTime desc")
    Page<PostDto> findAllPostDto(Pageable pageable);

    @Query("select p from Post p"
            + " join fetch p.chats c"
            + " where p.id = :postId")
    Post findAllWithChatByIdUseFetch(Long postId);
}