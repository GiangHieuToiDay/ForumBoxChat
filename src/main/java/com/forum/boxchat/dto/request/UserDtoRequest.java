package com.forum.boxchat.dto.request;

import com.forum.boxchat.model.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;


@Data
public class UserDtoRequest {

    @NotNull
    @NotEmpty
    @NotBlank
    @Size(min = 1, max = 225)
    private String name;

    @NotBlank
    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotBlank
    @NotNull
    @NotEmpty
    @Size(min = 6, max = 225)
    private String password;

    private String avatarUrl;

    private Set<Role> roles;
}
