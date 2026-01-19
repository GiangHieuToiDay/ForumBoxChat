package com.forum.boxchat.config.websocket;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // tính năng xử lý tin nhắn với môi gới là message broker
@RequiredArgsConstructor
public class SocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {  // đăng ký end point để cilent connect vào
        registry.addEndpoint("/ws") // đường dẫn này chính là đường dẫn mà cilent connect với web socket
                .setAllowedOriginPatterns("*") //cho phép tất cả các nguồn (Origins) truy cập vào.
                                              // Điều này quan trọng để tránh lỗi CORS khi Frontend và Backend chạy ở domain khác nhau.
                .withSockJS(); //Kích hoạt SockJS. Đây là một thư viện dự phòng. Nếu trình duyệt của người dùng quá cũ không hỗ trợ WebSocket,
                              // nó sẽ tự động chuyển sang các cơ chế khác như HTTP Long Polling để đảm bảo kết nối không bị gián đoạn.
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) { // đây là nơi cấu hình luồng đi của tin nhắn
        // trong kiến trúc stomp message khi gửi sẽ đến ngã 3 lựa chọn
        // đi thẳng tới broker ( là cái end point cấu hình ở trên): k xử lý logic gì cả
        // còn setApplicationDestinationPrefixes: đánh dấu tiền tố để đánh dấu khi cilent gửi nó sẽ đi vào controller
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic", "/queue");
        /**
         * Định nghĩa các tiền tố cho các tin nhắn từ Server gửi về Client.
         *
         * /topic: Thường dùng cho cơ chế Pub/Sub (một người gửi, nhiều người nhận - như Group Chat).
         *
         * /queue: Thường dùng cho tin nhắn User-to-User (gửi trực tiếp cho 1 người cụ thể)
         **/
        registry.setUserDestinationPrefix("/user");
        //thiết lập dùng để gửi tin nhắn riêng tư (Private Message) đến một người dùng cụ thể, thay vì gửi cho tất cả mọi người (Public).
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketAuthInterceptor); // Đăng ký lớp gác cổng này ở đây
    }

}
