package com.kimsehw.myteam.controller;

import com.kimsehw.myteam.dto.MemberFormDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/members/new")
    public String newMemberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "members/new";
    }

    @PostMapping("/members/new")
    public String newMember(@Valid MemberFormDto memberFormDto,
                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "members/new";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            Long l = memberService.saveMember(member, passwordEncoder);
        } catch (IllegalStateException e) {
            //에러 메시지 뷰로 전달 -> script 에서 javaScript 통해 스프링으로부터 (모델에 담겨)넘어온 errorMessage 처리
            model.addAttribute("errorMessage", e.getMessage());
            return "members/new";
        }

        return "redirect:/";
    }
}
