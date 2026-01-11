package com.forum.boxchat.controller;


import com.forum.boxchat.dto.request.PostDtoRequest;
import com.forum.boxchat.dto.respone.PostDtoResponse;
import com.forum.boxchat.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    public final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDtoResponse>> getAllPosts(){
        return ResponseEntity.ok(postService.findAllPost());
    }

    @PostMapping
    public ResponseEntity<PostDtoResponse> addPost(@RequestBody PostDtoRequest postDtoRequest){
        return ResponseEntity.ok(postService.createPost(postDtoRequest));
    }


}
