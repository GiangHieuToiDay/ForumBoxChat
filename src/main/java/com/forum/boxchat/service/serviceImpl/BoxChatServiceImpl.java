package com.forum.boxchat.service.serviceImpl;


import com.forum.boxchat.dto.request.BoxChatDtoRequest;
import com.forum.boxchat.dto.respone.BoxChatDtoResponse;
import com.forum.boxchat.exception.AppException;
import com.forum.boxchat.exception.ErrorCode;
import com.forum.boxchat.mapper.BoxChatMapper;
import com.forum.boxchat.model.entity.BoxChat;
import com.forum.boxchat.model.entity.User;
import com.forum.boxchat.model.enums.BoxChatType;
import com.forum.boxchat.repository.BoxChatRepository;
import com.forum.boxchat.repository.UserRepository;
import com.forum.boxchat.service.BoxChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoxChatServiceImpl implements BoxChatService {

    public final BoxChatRepository bcRepository;
    public final BoxChatMapper boxChatMapper;
    public final UserRepository userRepository;

    @Override
    public List<BoxChatDtoResponse> getALlBoxChat() {

        List<BoxChat> boxChats = bcRepository.findAll();

        return boxChats.stream()
                .map(boxChatMapper :: toResponse)
                .toList();
    }

    @Override
    public BoxChatDtoResponse createBoxChat(BoxChatDtoRequest dto) {

        String type = dto.getType().name();

        BoxChat boxChat = boxChatMapper.toEntity(dto);

        String idRaw = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID uuid = UUID.fromString(idRaw);
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));



        boxChat.setCreatedBy(user);
        bcRepository.save(boxChat);

        return boxChatMapper.toResponse(boxChat);
    }


    @Override
    public BoxChatDtoResponse updateBoxChat(int id,BoxChatDtoRequest boxChatDtoRequest) {

        BoxChat boxChat = bcRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOX_CHAT_NOT_FOUND));

        boxChat.setName(boxChatDtoRequest.getName());
        return boxChatMapper.toResponse(boxChat);
    }

    @Override
    public void deleteBoxChat(int id) {

        BoxChat boxChat = bcRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOX_CHAT_NOT_FOUND));

        String idRaw = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID uuid = UUID.fromString(idRaw);
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if( !boxChat.getCreatedBy().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.YOU_NOT_HAVE_AUTHOR_TO_DO_ACTION);
        }

        bcRepository.deleteById(id);
    }

    @Override
    public BoxChatDtoResponse getBoxChatById(int id) {
        BoxChat boxChat = bcRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BOX_CHAT_NOT_FOUND));

        return boxChatMapper.toResponse(boxChat);
    }


}
