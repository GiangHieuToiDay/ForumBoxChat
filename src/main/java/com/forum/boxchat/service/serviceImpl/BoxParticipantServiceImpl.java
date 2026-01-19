package com.forum.boxchat.service.serviceImpl;

import com.forum.boxchat.dto.request.BoxParticipantDtoRequest;
import com.forum.boxchat.dto.respone.BoxParticipantDtoResponse;
import com.forum.boxchat.dto.respone.UserDtoRespone;
import com.forum.boxchat.exception.AppException;
import com.forum.boxchat.exception.ErrorCode;
import com.forum.boxchat.mapper.BoxParticipantMapper;
import com.forum.boxchat.mapper.UserMapper;
import com.forum.boxchat.model.entity.BoxChat;
import com.forum.boxchat.model.entity.BoxParticipant;
import com.forum.boxchat.model.entity.User;
import com.forum.boxchat.repository.BoxChatRepository;
import com.forum.boxchat.repository.BoxParticipantRepository;
import com.forum.boxchat.repository.UserRepository;
import com.forum.boxchat.service.BoxParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.List.*;


@Service
@RequiredArgsConstructor
public class BoxParticipantServiceImpl implements BoxParticipantService {

    public final BoxParticipantRepository bpRepo;
    public final BoxParticipantMapper bpMapper;
    public final UserRepository userRepo;
    public final BoxChatRepository boxChatRepo;
    public final UserMapper userMapper;

    @Override
    public List<BoxParticipantDtoResponse> findByBoxId(int boxId) {
        List<BoxParticipant> list = bpRepo.findByBoxChat_Id(boxId);

        if (list.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND_BOX_PARTICIPANT);
        }

        return list.stream()
                .map(bpMapper::toResponse)
                .toList();
    }


    @Override
    public List<BoxParticipantDtoResponse> findByUserId(UUID userId) {

        List<BoxParticipant> boxParticipants = bpRepo.findByUser_Id(userId);

        if (boxParticipants.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND_BOX_PARTICIPANT);
        }

        return boxParticipants.stream()
                .map(bpMapper :: toResponse)
                .toList();
    }

    @Override
    public BoxParticipantDtoResponse create(BoxParticipantDtoRequest request) {

        BoxParticipant boxParticipant = new BoxParticipant();

        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boxParticipant.setUser(user);

        BoxChat boxChat = boxChatRepo.findById(request.getBoxChatId())
                .orElseThrow(() -> new AppException(ErrorCode.BOX_CHAT_NOT_FOUND));
        boxParticipant.setBoxChat(boxChat);
        bpRepo.save(boxParticipant);

        return bpMapper.toResponse(boxParticipant);
    }

    @Override
    public BoxParticipantDtoResponse update(int id, BoxParticipantDtoRequest request) {
        return null;
    }

    @Override
    public void delete(Integer id) {
        bpRepo.deleteById(id);
    }

    @Override
    public BoxParticipantDtoResponse addUserToBox(BoxParticipantDtoRequest req) {

        if ( bpRepo.existsByBoxChat_IdAndUser_Id(req.getBoxChatId(), req.getUserId())) {
            throw new AppException(ErrorCode.USER_ALREADY_IN_BOX);
        }

        BoxChat box = boxChatRepo.findById(req.getBoxChatId()).orElseThrow();
        User user = userRepo.findById(req.getUserId()).orElseThrow();

        BoxParticipant bp = new BoxParticipant();
        bp.setBoxChat(box);
        bp.setUser(user);

        return bpMapper.toResponse(bpRepo.save(bp));
    }

    @Override
    public void removeUserFromBox(int boxId, UUID userId) {

        BoxParticipant box = bpRepo.findByBoxChat_IdAndUser_Id(boxId, userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_BOX_PARTICIPANT));

        bpRepo.delete(box);
    }

}
