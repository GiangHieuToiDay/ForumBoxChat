package com.forum.boxchat.mapper;

import com.forum.boxchat.dto.request.PostDtoRequest;
import com.forum.boxchat.dto.respone.PostDtoResponse;
import com.forum.boxchat.model.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "user.name", target = "authorName")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "commentCount", expression = "java(post.getComments() != null ? post.getComments().size() : 0)")
    PostDtoResponse toResponse(Post post);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    Post toEntity(PostDtoRequest postDtoRequest);
}