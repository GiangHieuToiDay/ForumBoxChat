package com.forum.boxchat.service;

import com.forum.boxchat.dto.request.MessageDtoRequest;
import com.forum.boxchat.dto.respone.MessageDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageDtoResponse sendMessage(MessageDtoRequest request);

    Page<MessageDtoResponse> getMessagesByBox(int boxId, Pageable pageable);

    List<MessageDtoResponse> getMessagesBefore(int boxId, LocalDateTime beforeTime, int limit);

    MessageDtoResponse getLastMessage(int boxId);

    void deleteMessageForMe(int messageId, UUID userId);

    void revokeMessage(int messageId, UUID userId);

    void markMessagesAsRead(int boxId, UUID userId);

    long countUnreadMessages(int boxId, UUID userId);

//    MessageDtoResponse editMessage(int messageId, String newContent, Long userId);

//    Page<MessageDtoResponse> searchMessages(Long boxId, String keyword, Pageable pageable);
}

