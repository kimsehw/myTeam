package com.kimsehw.myteam.controller;

import com.kimsehw.myteam.constant.PostType;
import com.kimsehw.myteam.dto.post.PostFormDto;
import com.kimsehw.myteam.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts/new")
    public String postForm(Model model) {
        model.addAttribute("postForm", new PostFormDto());
        model.addAttribute("postTypes", PostType.values());
        return "post/postForm";
    }

    @PostMapping("/posts/new")
    public String postForm(@Valid PostFormDto postFormDto, BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("postTypes", PostType.values());
            return "post/postForm";
        }

        postService.savePost(postFormDto);

        return "post/postForm";
    }
}
