package com.forum.boxchat.dto.respone;


import com.forum.boxchat.model.entity.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDtoRespone {
    private UUID id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private Set<Role> roles;
}
