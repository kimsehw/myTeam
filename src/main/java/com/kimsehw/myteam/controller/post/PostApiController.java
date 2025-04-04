package com.kimsehw.myteam.controller.post;

import com.kimsehw.myteam.application.PostFacade;
import com.kimsehw.myteam.dto.post.ChatFormDto;
import com.kimsehw.myteam.dto.post.PostFormDto;
import com.kimsehw.myteam.exception.FieldErrorException;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
            com.kimsehw.myteam.domain.FieldError fieldError = e.getFieldError();
            return ResponseEntity.badRequest().body(fieldError.getErrorMessage());
        }
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @DeleteMapping("/posts/{postId}")
    private ResponseEntity delete(@PathVariable("postId") Long postId) {
        try {
            postFacade.deletePost(postId);
        } catch (RuntimeException e) {
            log.info(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok(Collections.emptyMap());
    }

    @PatchMapping("/posts/new/{postId}")
    private ResponseEntity update(@PathVariable("postId") Long postId,
                                  @Valid @RequestBody PostFormDto postFormDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            postFacade.updatePost(postId, postFormDto);
        } catch (RuntimeException e) {
            log.info(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
        return ResponseEntity.ok(Collections.emptyMap());
    }
}
