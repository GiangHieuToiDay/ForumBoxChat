package com.forum.boxchat.mapper;


import com.forum.boxchat.dto.request.CommentDtoRequest;
import com.forum.boxchat.dto.respone.CommentDtoResponse;
import com.forum.boxchat.model.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "content", target = "content")

    @Mapping(source = "post.id", target = "postId")

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "username")
    @Mapping(source = "user.avatarUrl", target = "avatarUrl")

    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updateAt", target = "updatedAt")
    CommentDtoResponse toResponseDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    Comment toEntity(CommentDtoRequest commentDtoRequest);


}
