package com.forum.boxchat.config.websocket;

import com.forum.boxchat.config.CustomJwtDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final CustomJwtDecoder customJwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        //Chuyển các tin nhắn thông thường thành dạng stomp để đọc được các header: author
        // stomp là viết tắt của Simple text orientated messaging protocol
        // => giao thức nhắn tin đơn giaản chạy đè lên trên over giao thức web socket
        // web socket => liên kết giữa trình duyệt và sever
        // stomp => ngôn ngữ mà 2 bên (cilent, sever) dùng để nói chuyện
        //Giao thức STOMP có các thông tin quan trọng nằm trong Header mà một tin nhắn thường không có. Dùng StompHeaderAccessor, bạn có thể lấy được:
        //Session ID: accessor.getSessionId() (ID của kết nối hiện tại).
        //User: accessor.getUser() (Thông tin người dùng đã được xác thực).
        //Destination: accessor.getDestination() (Tin nhắn này đang gửi đến đâu, ví dụ: /topic/messages).
        //Command: accessor.getCommand() (Lệnh STOMP là gì: CONNECT, SEND, SUBSCRIBE, DISCONNECT...).
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                String token = authHeader.substring(7);

                Jwt jwt = customJwtDecoder.decode(token);

                Authentication authentication =
                        jwtAuthenticationConverter.convert(jwt);

                accessor.setUser(authentication);
            }
        }
        return message;
    }
}

