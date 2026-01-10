package com.forum.boxchat.sercurity;


import com.forum.boxchat.dto.request.IntrospectRequest;
import com.forum.boxchat.dto.respone.AuthResponse;
import com.forum.boxchat.dto.respone.IntrospectResponse;
import com.forum.boxchat.exception.AppException;
import com.forum.boxchat.exception.ErrorCode;
import com.forum.boxchat.mapper.UserMapper;
import com.forum.boxchat.model.entity.Role;
import com.forum.boxchat.model.entity.User;
import com.forum.boxchat.repository.RoleRepository;
import com.forum.boxchat.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    public final UserRepository userRepository;
    public final RoleRepository roleRepository;
    public final UserMapper userMapper;
    public final PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerkey}")
    private String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest introspectRequest)
            throws JOSEException, ParseException {
        var token = introspectRequest.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date experytime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var vertifed = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(vertifed && experytime.after(new Date()))
                .build();
    }

    public String genarateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId().toString())
                .issuer("gianghieu.com")
                .issueTime(new Date())
                .expirationTime( new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
//                .expirationTime( new Date(
//                        Instant.now().plus(10, ChronoUnit.SECONDS).toEpochMilli()
//                ))
                .claim("roles",buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header,payload);
        try{
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        }catch (JOSEException e){
            log.error("Cannot create token",e.getMessage());
            throw new AppException(ErrorCode.CANOT_CREATE_TOKEN);
        }
    }

    private String buildScope(User user){
        StringJoiner scope = new StringJoiner(" ");

        Set<Role> roles = user.getRoles();
        for(Role role : roles){
            scope.add(role.getName().name());
        }

        if (user.isActive()) {
            scope.add("VERIFIED"); // Thêm quyền đã xác thực mail
        }

        return scope.toString();
    }

    public AuthResponse authenticateUser(String email, String rawPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = genarateToken(user);

        Set<String> roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        return AuthResponse.builder()
                .token(token)
                .isValid(true)
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getName())
                .roles(roles)
                .build();
    }

    public void changePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }




}
