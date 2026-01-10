package com.forum.boxchat.repository;

import com.forum.boxchat.model.entity.Role;
import com.forum.boxchat.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, RoleName> {
    Optional<Role> findByName(RoleName name);
}

