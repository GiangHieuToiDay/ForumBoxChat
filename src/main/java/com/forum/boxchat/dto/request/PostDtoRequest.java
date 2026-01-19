package com.forum.boxchat.dto.request;


import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDtoRequest {

    @Size(min = 1, max = 100)
    private String title;


    @Size(min = 1, max = 1000)
    private String content;

    private int categoryId;


}
