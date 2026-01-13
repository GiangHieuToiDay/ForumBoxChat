package com.forum.boxchat.controller;


import com.forum.boxchat.dto.request.PostDtoRequest;
import com.forum.boxchat.dto.respone.PostDtoResponse;
import com.forum.boxchat.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('USER') and hasRole('VERIFIED')")
    @PostMapping
    public ResponseEntity<PostDtoResponse> addPost(@RequestBody PostDtoRequest postDtoRequest){
        return ResponseEntity.ok(postService.createPost(postDtoRequest));
    }

    @PreAuthorize("hasRole('USER') and hasRole('VERIFIED')")
    @PatchMapping
    public ResponseEntity<PostDtoResponse> updatePost(@PathVariable int id, @RequestBody PostDtoRequest postDtoRequest){
         return ResponseEntity.ok(postService.updatePost(id,postDtoRequest));
    }

    @PreAuthorize("(hasRole('USER') and hasRole('VERIFIED')) or hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<PostDtoResponse> deletePost(@PathVariable int id){
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/title")
    public ResponseEntity<List<PostDtoResponse>> getAllPostsByTitle(@RequestParam String title){
        return ResponseEntity.ok(postService.findPostByTitle(title));
    }




}
