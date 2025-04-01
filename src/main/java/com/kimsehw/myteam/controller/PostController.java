package com.kimsehw.myteam.controller;

import com.kimsehw.myteam.constant.post.PostType;
import com.kimsehw.myteam.dto.post.PostDto;
import com.kimsehw.myteam.dto.post.PostFormDto;
import com.kimsehw.myteam.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PostController {

    public static final int MAX_POST_SHOW = 10;
    private final PostService postService;

    @GetMapping("/posts/new")
    public String postForm(Model model) {
        model.addAttribute("postForm", new PostFormDto());
        model.addAttribute("postTypes", PostType.values());
        return "post/postForm";
    }

    @PostMapping("/posts/new")
    public String postRegistry(@Valid PostFormDto postFormDto, BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("postTypes", PostType.values());
            return "post/postForm";
        }

        postService.savePost(postFormDto);

        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String postView(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, MAX_POST_SHOW);
        Page<PostDto> posts = postService.getPosts(pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("maxPage", MAX_POST_SHOW);
        model.addAttribute("page", pageable.getPageNumber());
        return "post/postList";
    }
}
