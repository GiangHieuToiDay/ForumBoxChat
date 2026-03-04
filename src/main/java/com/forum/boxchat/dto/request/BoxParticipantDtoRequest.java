package com.forum.boxchat.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class BoxParticipantDtoRequest {

    @NotNull
    private Integer boxChatId;

    @NotNull
    private UUID userId;


}
