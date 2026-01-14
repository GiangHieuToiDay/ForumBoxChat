package com.forum.boxchat.dto.respone;


import com.forum.boxchat.model.enums.BoxChatType;
import lombok.Data;

import java.util.UUID;

@Data
public class BoxChatDtoResponse {

    private int id;
    private String name;
    private BoxChatType type;
    private UUID createdBy;


}
