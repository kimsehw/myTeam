package com.kimsehw.myteam.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Log
public class MainController {

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/test")
    public String test() {
        //log.info(principal.getName());
        return "main";
    }
}
