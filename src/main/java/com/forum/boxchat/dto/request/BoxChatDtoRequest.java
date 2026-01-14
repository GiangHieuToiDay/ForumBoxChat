package com.forum.boxchat.dto.request;


import com.forum.boxchat.model.enums.BoxChatType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BoxChatDtoRequest {


    @NotNull( message = "Name can not null")
    @NotEmpty( message = "Name can not empty")
    private String name;

    @NotNull( message = "Type can not null")
    @NotEmpty( message = "Type can not empty")
    @NotBlank( message = "Type can not blank")
    private BoxChatType type;


}
