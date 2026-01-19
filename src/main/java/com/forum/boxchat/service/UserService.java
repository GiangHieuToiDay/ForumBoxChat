package com.forum.boxchat.service;

import com.forum.boxchat.dto.request.UserDtoRequest;
import com.forum.boxchat.dto.respone.UserDtoRespone;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;



public interface UserService {
    List<UserDtoRespone> findAllUsers();
    UserDtoRespone createUser(UserDtoRequest userDtoRequest);
    UserDtoRespone updateUser(UserDtoRequest userDtoRequest);
    void deleteUser(UUID uuid);
    String verifyAccount(UUID token);
    UserDtoRespone getUser(UUID token);
}
