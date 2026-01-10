package com.forum.boxchat.exception;

public enum ErrorCode {
    USER_NOT_FOUND(1001, "User not found"),
    USERNAME_ALREADY_EXISTS(1002, "Username already exists"),
    INVALID_PASSWORD(1003, "Invalid password"),
    UNAUTHORIZED(1004, "Unauthorized"),
    FORBIDDEN(1005, "Forbidden"),
    USER_LIST_EMPTY(1006,"List is Empty"),
    NOT_FOUND_ROLE(1007,"Role not found"),
    EMAIL_ALREADY_EXISTS(1008,"Email already exists"),
    INVALID_CREDENTIALS(1009, "Invalid credentials"),
    CANOT_CREATE_TOKEN(1010, "Can not create token jwt"),
    CANOT_SEND_EMAIL(1011, "Can not send email"),
    TOKEN_NOT_FOUND(1012, "Token not found"),
    TOKEN_EXPIRED(1013, "Token expired");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
