package com.kimsehw.myteam.controller;

import com.kimsehw.myteam.application.PostFacade;
import com.kimsehw.myteam.constant.post.PostType;
import com.kimsehw.myteam.dto.member.MyTeamsInfoDto;
import com.kimsehw.myteam.dto.post.PostDto;
import com.kimsehw.myteam.dto.post.PostFormDto;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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
@Log
public class PostController {

    public static final int MAX_POST_SHOW = 10;

    private final PostFacade postFacade;

    @GetMapping("/posts/new")
    public String postForm(Model model, Principal principal) {
        String email = principal.getName();
        List<MyTeamsInfoDto> myTeams = postFacade.findMyTeamsInfoByEmail(email);
        model.addAttribute("postForm", new PostFormDto());
        model.addAttribute("myTeams", myTeams);
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

        postFacade.savePost(postFormDto);

        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String postView(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, MAX_POST_SHOW);
        Page<PostDto> posts = postFacade.getPosts(pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("maxPage", MAX_POST_SHOW);
        model.addAttribute("page", pageable.getPageNumber());
        return "post/postList";
    }
}
