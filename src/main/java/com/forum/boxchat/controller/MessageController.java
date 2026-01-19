package com.forum.boxchat.controller;

import com.forum.boxchat.dto.request.MessageDtoRequest;
import com.forum.boxchat.dto.respone.MessageDtoResponse;
import com.forum.boxchat.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public MessageDtoResponse sendMessage(
            @Valid @RequestBody MessageDtoRequest request
    ) {
        return messageService.sendMessage(request);
    }

    @GetMapping("/box/{boxId}")
    public Page<MessageDtoResponse> getMessages(
            @PathVariable int boxId,
            Pageable pageable
    ) {
        return messageService.getMessagesByBox(boxId, pageable);
    }
}

