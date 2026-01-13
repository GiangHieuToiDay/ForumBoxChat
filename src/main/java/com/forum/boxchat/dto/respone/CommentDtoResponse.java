package com.forum.boxchat.dto.respone;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentDtoResponse {

    private int id;
    private String content;

    private Long postId;

    private UUID userId;
    private String username;
    private String avatarUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

