package com.forum.boxchat.dto.respone;


import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
public class AuthResponse {
    private String token;
    private boolean isValid;

    // Thông tin Người dùng
    private UUID id;
    private String email;
    private String username;
    private Set<String> roles;
    // ...
}
