package com.forum.boxchat.mapper;

import com.forum.boxchat.dto.request.BoxParticipantDtoRequest;
import com.forum.boxchat.dto.respone.BoxParticipantDtoResponse;
import com.forum.boxchat.model.entity.BoxParticipant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper( componentModel = "spring")
public interface BoxParticipantMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "boxChat.id" , target = "boxChatId")
    BoxParticipantDtoResponse toResponse(BoxParticipant boxParticipant);

    BoxParticipant toEntity(BoxParticipantDtoRequest dto);

}
