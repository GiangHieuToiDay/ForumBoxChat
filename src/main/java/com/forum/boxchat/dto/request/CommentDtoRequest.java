package com.forum.boxchat.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDtoRequest {

    @NotBlank(message = "Content can not blank")
    @Size(max = 255)
    private String content;

    @NotNull
    private int postId;
}

