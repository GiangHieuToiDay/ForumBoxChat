package com.forum.boxchat.service.serviceImpl;

import com.forum.boxchat.dto.request.MessageDtoRequest;
import com.forum.boxchat.dto.respone.MessageDtoResponse;
import com.forum.boxchat.exception.AppException;
import com.forum.boxchat.exception.ErrorCode;
import com.forum.boxchat.mapper.MessageMapper;
import com.forum.boxchat.model.entity.Message;
import com.forum.boxchat.model.entity.User;
import com.forum.boxchat.repository.BoxChatRepository;
import com.forum.boxchat.repository.BoxParticipantRepository;
import com.forum.boxchat.repository.MessageRepository;
import com.forum.boxchat.repository.UserRepository;
import com.forum.boxchat.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    public final MessageRepository msgRepo;
    public final UserRepository userRepo;
    public final BoxChatRepository boxChatRepo;
    public final BoxParticipantRepository boxParticipantRepo;
    public final MessageMapper messageMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public MessageDtoResponse sendMessage(MessageDtoRequest request) {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        UUID senderId = UUID.fromString(auth.getName());
        log.info(senderId.toString());

        User sender = userRepo.findById(senderId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));


        var boxChat = boxChatRepo.findById(request.getBoxChatId())
                .orElseThrow(() -> new AppException(ErrorCode.BOX_CHAT_NOT_FOUND));


        int checkUserInBox = 0;
        List<User> list = boxParticipantRepo.getAllUserByBoxChatId(boxChat.getId());
        for (User u : list) {
            if (u.getId().equals(senderId)) {
                checkUserInBox = 1;
            }
        }


        if (checkUserInBox == 0) {
            throw new AppException(ErrorCode.USER_NOT_FOUND_IN_BOX);
        }

        Message message = new Message();
        message.setBoxChat(boxChat);
        message.setUser(sender);
        message.setContent(request.getContent());
        message.setMessageType(request.getMessageType());

        Message saved = msgRepo.save(message);

        MessageDtoResponse response = messageMapper.toResponse(saved);

        // PUSH REALTIME CHO TẤT CẢ USER TRONG BOX
        messagingTemplate.convertAndSend(
                "/topic/box/" + boxChat.getId(),
                response
        );

        return response;

        //return messageMapper.toResponse(saved);
    }


    @Override
    public Page<MessageDtoResponse> getMessagesByBox(int boxId, Pageable pageable) {

        Page<Message> page = msgRepo.findByBoxChatId(boxId, pageable);

        return page.map(messageMapper::toResponse);
    }

    @Override
    public List<MessageDtoResponse> getMessagesBefore(int boxId, LocalDateTime beforeTime, int limit) {
        return List.of();
    }

    @Override
    public MessageDtoResponse getLastMessage(int boxId) {
        return null;
    }

    @Override
    public void deleteMessageForMe(int messageId, UUID userId) {

    }

    @Override
    public void revokeMessage(int messageId, UUID userId) {

    }

    @Override
    public void markMessagesAsRead(int boxId, UUID userId) {

    }

    @Override
    public long countUnreadMessages(int boxId, UUID userId) {
        return 0;
    }
}
