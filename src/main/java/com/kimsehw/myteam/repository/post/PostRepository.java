package com.kimsehw.myteam.repository.post;

import com.kimsehw.myteam.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
