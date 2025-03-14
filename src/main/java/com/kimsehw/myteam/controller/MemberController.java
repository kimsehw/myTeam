package com.kimsehw.myteam.controller;

import com.kimsehw.myteam.dto.member.MemberFormDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Log
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
            String email = memberService.saveMember(member);
        } catch (IllegalStateException e) {
            //에러 메시지 뷰로 전달 -> script 에서 javaScript 통해 스프링으로부터 (모델에 담겨)넘어온 errorMessage 처리
            model.addAttribute("errorMessage", e.getMessage());
            return "members/new";
        }

        return "redirect:/";
    }

    @GetMapping("/members/login")
    public String login() {
        return "members/login";
    }

    @GetMapping("/members/login/unauthorized")
    public String login(Model model) {
        model.addAttribute("errorMessage", "접근 권한이 없습니다. 로그인이 필요합니다.");
        return "members/login";
    }

    @GetMapping("/members/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "members/login";
    }
}
