package com.forum.boxchat.config;


import com.forum.boxchat.exception.AppException;
import com.forum.boxchat.exception.ErrorCode;
import com.forum.boxchat.model.entity.Role;
import com.forum.boxchat.model.entity.User;
import com.forum.boxchat.model.enums.RoleName;
import com.forum.boxchat.repository.RoleRepository;
import com.forum.boxchat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    public final UserRepository userRepository;
    public final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        final String ADMIN_EMAIL = "admin@demo.com";
        final String ADMIN_PASSWORD = "1234";

        if (userRepository.findByEmail(ADMIN_EMAIL) == null) {

            User adminUser = new User();
            adminUser.setName("admin");
            adminUser.setEmail(ADMIN_EMAIL);
            adminUser.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            adminUser.setActive(true);

            Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_ROLE));

            adminUser.setRoles(Set.of(adminRole));
            userRepository.save(adminUser);

            log.info("Admin user created successfully with email: {}", ADMIN_EMAIL);
        } else {
            log.info("Admin user already exists.");
        }
    }
    }
