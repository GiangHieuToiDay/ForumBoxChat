package com.forum.boxchat.service;

import com.forum.boxchat.dto.request.BoxChatDtoRequest;
import com.forum.boxchat.dto.respone.BoxChatDtoResponse;

import java.util.List;

public interface BoxChatService {

    List<BoxChatDtoResponse> getALlBoxChat();

    BoxChatDtoResponse createBoxChat(BoxChatDtoRequest boxChatDtoRequest);

    BoxChatDtoResponse updateBoxChat(int id, BoxChatDtoRequest boxChatDtoRequest);

    List<BoxChatDtoResponse> findAllBoxChat();

    void deleteBoxChat(int id);

    BoxChatDtoResponse getBoxChatById(int id);

}
