package com.forum.boxchat.dto.respone;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
public class PostDtoResponse {
    private int id;
    private String title;
    private String content;
    private String slug;
    private String authorName;
    private UUID authorId;
    private String categoryName;
    private LocalDateTime createdAt;
    private int commentCount;
    private String imageUrl;
}
