package com.forum.boxchat.mapper;


import com.forum.boxchat.dto.request.UserDtoRequest;
import com.forum.boxchat.dto.respone.UserDtoRespone;
import com.forum.boxchat.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    //UserDtoRespone toResponse(User user);
    UserDtoRespone toResponse(User user);
    //User toEntity(UserDtoRequest userDtoRequest);
    User toEntity(UserDtoRequest userDtoRequest);

}
