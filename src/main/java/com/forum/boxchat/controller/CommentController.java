package com.forum.boxchat.controller;


import com.forum.boxchat.dto.request.CommentDtoRequest;
import com.forum.boxchat.dto.respone.CommentDtoResponse;
import com.forum.boxchat.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    public final CommentService commentService;

    @GetMapping()
    public ResponseEntity<List<CommentDtoResponse>> getComments(@RequestParam("postId") int postId) {
        return  ResponseEntity.ok(commentService.findByPost_Id(postId));
    }

    @PreAuthorize("hasRole('USER') and hasRole('VERIFIED')")
    @PostMapping
    public ResponseEntity<CommentDtoResponse> addComment(@RequestBody CommentDtoRequest commentDtoRequest){
        return ResponseEntity.ok(commentService.createComment(commentDtoRequest));
    }

    @PatchMapping
    public ResponseEntity<CommentDtoResponse> updateComment(@RequestParam int id,@RequestBody CommentDtoRequest commentDtoRequest){
        return ResponseEntity.ok(commentService.updateComment(id, commentDtoRequest));
    }

    @DeleteMapping
    public ResponseEntity<CommentDtoResponse> deleteComment(@RequestParam int id){
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }







}
