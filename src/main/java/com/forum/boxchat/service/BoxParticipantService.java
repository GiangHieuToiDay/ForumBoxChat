package com.forum.boxchat.service;


import com.forum.boxchat.dto.request.BoxParticipantDtoRequest;
import com.forum.boxchat.dto.respone.BoxParticipantDtoResponse;
import com.forum.boxchat.dto.respone.UserDtoRespone;
import com.forum.boxchat.model.entity.BoxParticipant;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


public interface BoxParticipantService {

    List<BoxParticipantDtoResponse> findByBoxId(int boxId);

    List<BoxParticipantDtoResponse> findByUserId(UUID userId);

    BoxParticipantDtoResponse create(BoxParticipantDtoRequest request);

    BoxParticipantDtoResponse update(int id, BoxParticipantDtoRequest request);

    void delete(Integer id);

    BoxParticipantDtoResponse addUserToBox(BoxParticipantDtoRequest request);

    void removeUserFromBox(int boxId, UUID userId);


}

