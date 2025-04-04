package com.kimsehw.myteam.controller.post;

import com.kimsehw.myteam.application.PostFacade;
import com.kimsehw.myteam.domain.FieldError;
import com.kimsehw.myteam.dto.post.ChatFormDto;
import com.kimsehw.myteam.exception.FieldErrorException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log
public class PostApiController {

    private final PostFacade postFacade;

    @PostMapping("/posts/{postId}/addChat")
    private ResponseEntity addChat(@PathVariable("postId") Long postId, @RequestBody ChatFormDto chatFormDto) {
        try {
            postFacade.addChat(postId, chatFormDto);
        } catch (FieldErrorException e) {
            FieldError fieldError = e.getFieldError();
            return ResponseEntity.badRequest().body(fieldError.getErrorMessage());
        }
        return ResponseEntity.ok(Collections.emptyMap());
    }
}
