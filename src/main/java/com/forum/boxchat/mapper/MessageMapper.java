package com.forum.boxchat.mapper;


import com.forum.boxchat.dto.request.MessageDtoRequest;
import com.forum.boxchat.dto.respone.MessageDtoResponse;
import com.forum.boxchat.model.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MessageMapper {


    @Mapping(source = "boxChat.id", target = "boxChatId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "username")
    MessageDtoResponse toResponse(Message message);


//    @Mapping(source = "boxChatId", target = "boxChat")
//    @Mapping(source = "userId", target = "user")
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
    Message toEntity(MessageDtoRequest dto);






}
