package com.forum.boxchat.controller;


import com.forum.boxchat.dto.request.BoxChatDtoRequest;
import com.forum.boxchat.dto.respone.BoxChatDtoResponse;
import com.forum.boxchat.service.BoxChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/box-chat")
@RequiredArgsConstructor
public class BoxChatController {

    public final BoxChatService boxChatService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BoxChatDtoResponse>> getBoxChat(){
        return ResponseEntity.ok(boxChatService.getALlBoxChat());
    }

    @PreAuthorize("hasRole('USER') and hasRole('VERIFIED')")
    @GetMapping("/")
    public ResponseEntity<BoxChatDtoResponse> getBoxChatById(@RequestParam int id){
        return ResponseEntity.ok(boxChatService.getBoxChatById(id));
    }

    @PreAuthorize("hasRole('USER') and hasRole('VERIFIED')")
    @PostMapping
    public ResponseEntity<BoxChatDtoResponse> createBoxChat(@RequestBody BoxChatDtoRequest boxChatDtoRequest){
        return ResponseEntity.ok(boxChatService.createBoxChat(boxChatDtoRequest));
    }

    @PreAuthorize("hasRole('USER') and hasRole('VERIFIED')")
    @PatchMapping
    public ResponseEntity<BoxChatDtoResponse> updateBoxChat(@RequestParam int id,@RequestBody BoxChatDtoRequest boxChatDtoRequest){
        return ResponseEntity.ok(boxChatService.updateBoxChat(id, boxChatDtoRequest));
    }

    @PreAuthorize("(hasRole('USER') and hasRole('VERIFIED')) or hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<BoxChatDtoResponse> deleteBoxChat(@RequestParam int id){
        boxChatService.deleteBoxChat(id);
        return ResponseEntity.ok().build();

    }




}
