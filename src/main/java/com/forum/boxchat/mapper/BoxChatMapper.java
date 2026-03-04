package com.forum.boxchat.mapper;


import com.forum.boxchat.dto.request.BoxChatDtoRequest;
import com.forum.boxchat.dto.respone.BoxChatDtoResponse;
import com.forum.boxchat.model.entity.BoxChat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoxChatMapper {

    @Mapping(target = "createdBy", source = "createdBy.id")
    BoxChatDtoResponse toResponse(BoxChat boxChat);

    @Mapping(target = "id", ignore = true) // Luôn ignore ID khi tạo mới từ DTO
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "participants", ignore = true)
    BoxChat toEntity(BoxChatDtoRequest dto);


}
