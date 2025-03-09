package com.kimsehw.myteam.controller;

import com.kimsehw.myteam.dto.UserFormDto;
import com.kimsehw.myteam.entity.User;
import com.kimsehw.myteam.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/new")
    public String newUserForm(Model model) {
        model.addAttribute("userFormDto", new UserFormDto());
        return "users/new";
    }

    @PostMapping("/users/new")
    public String newUser(@Valid UserFormDto userFormDto,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "users/new";
        }

        try {
            User user = User.createMember(userFormDto);
            Long l = userService.saveUser(user);
        } catch (IllegalStateException e) {
            //에러 메시지 뷰로 전달 -> script 에서 javaScript 통해 스프링으로부터 (모델에 담겨)넘어온 errorMessage 처리
            model.addAttribute("errorMessage", e.getMessage());
            return "users/new";
        }

        return "redirect:/";
    }
}
