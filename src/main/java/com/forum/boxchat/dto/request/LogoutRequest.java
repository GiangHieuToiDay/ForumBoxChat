package com.forum.boxchat.dto.request;

import lombok.Data;

@Data
public class LogoutRequest {
    String refreshToken;
}
