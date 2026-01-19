package com.forum.boxchat.dto.respone;

import lombok.Data;

import java.util.UUID;


@Data
public class BoxParticipantDtoResponse {

    private int id;
    private Integer boxChatId;
    private UUID userId;

}
