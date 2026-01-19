package com.forum.boxchat.dto.request;

import com.forum.boxchat.model.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageDtoRequest {

    @NotNull
    private Integer boxChatId;

//    @NotNull
//    private Integer userId;

    @NotBlank
    private String content;

    @NotNull
    private MessageType messageType;
}
