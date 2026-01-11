package com.forum.boxchat.service.serviceImpl;


import com.forum.boxchat.dto.request.UserDtoRequest;
import com.forum.boxchat.dto.respone.UserDtoRespone;
import com.forum.boxchat.exception.AppException;
import com.forum.boxchat.exception.ErrorCode;
import com.forum.boxchat.mapper.UserMapper;
import com.forum.boxchat.model.entity.Role;
import com.forum.boxchat.model.entity.User;
import com.forum.boxchat.model.entity.VerificationToken;
import com.forum.boxchat.model.enums.RoleName;
import com.forum.boxchat.repository.RoleRepository;
import com.forum.boxchat.repository.UserRepository;
import com.forum.boxchat.repository.VerificationTokenRepository;
import com.forum.boxchat.service.MailService;
import com.forum.boxchat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    @NonFinal
    @Value("${app.baseurl}")
    private String baseurl;


    public final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    public final UserMapper userMapper;
    public final RoleRepository roleRepository;
    public final MailService mailService;
    public final VerificationTokenRepository vtRepository;

    @Override
    public List<UserDtoRespone> findAllUsers() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new AppException(ErrorCode.USER_LIST_EMPTY);
        }
        log.info("Users found: {}", users);

        return users.stream()
                .map(userMapper::toResponse)
                .toList();
    }


    @Override
    public UserDtoRespone createUser(UserDtoRequest userDtoRequest) {

        User user = userMapper.toEntity(userDtoRequest);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (checkDeuplicateEmail(userDtoRequest.getEmail()) == 0) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND_ROLE));

        user.setRoles(Set.of(userRole));

        user.setActive(false);
        User savedUser = userRepository.save(user);

        //Mail
        UUID uuid = UUID.randomUUID();
        String htmlRegister = """
                <h2>Verify account</h2>
                <p>Click link below:</p>
                <a href=\"""" + baseurl + "/auth/verify?token=" + uuid + """
                \">
                    Verify
                </a>
                """;
        mailService.sendMail(userDtoRequest.getEmail(), "Vertify Account", htmlRegister);
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(uuid);
        verificationToken.setUser(savedUser);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        vtRepository.save(verificationToken);




        return userMapper.toResponse(savedUser);
    }


    @Override
    public UserDtoRespone updateUser(UserDtoRequest userDtoRequest) {

        User user = userRepository.findByEmail(userDtoRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setName(userDtoRequest.getName());

        if (checkDeuplicateEmail(userDtoRequest.getEmail()) == 0 && !user.getEmail().equals(userDtoRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        user.setEmail(userDtoRequest.getEmail());
        user.setAvatarUrl(userDtoRequest.getAvatarUrl());
        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    public int checkDeuplicateEmail(String email) {
        int res = 1;
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                res = 0;
            }
        }
        return res;
    }

    @Override
    @Transactional
    public String verifyAccount(UUID token) {
        VerificationToken vToken = vtRepository.findByToken(token)
                .orElseThrow(
                        ()-> new AppException(ErrorCode.TOKEN_NOT_FOUND)
        );


        if (vToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            vtRepository.delete(vToken);
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        User user = vToken.getUser();
        user.setActive(true);
        userRepository.save(user);

        vtRepository.delete(vToken);

        return "Xác thực thành công! Giờ m có thể đăng nhập.";
    }

    @Override
    public UserDtoRespone getUser(UUID token) {
        User user = userRepository.findById(token)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toResponse(user);
    }


}
