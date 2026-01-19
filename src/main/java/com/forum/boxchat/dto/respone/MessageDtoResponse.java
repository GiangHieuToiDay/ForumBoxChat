package com.forum.boxchat.dto.respone;

import com.forum.boxchat.model.enums.MessageType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
public class MessageDtoResponse {
    private Integer id;

    private Integer boxChatId;

    private UUID  userId;
    private String username;

    private String content;

    private MessageType messageType;

    private LocalDateTime createdAt;
}
