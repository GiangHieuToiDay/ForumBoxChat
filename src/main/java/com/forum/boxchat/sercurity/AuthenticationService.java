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
import com.forum.boxchat.service.RefreshTokenService;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    public final UserRepository userRepository;
    public final RoleRepository roleRepository;
    public final UserMapper userMapper;
    public final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @NonFinal
    @Value("${jwt.signerkey}")
    private String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest introspectRequest)
            throws JOSEException, ParseException {
        var token = introspectRequest.getToken();
        SignedJWT signedJWT = SignedJWT.parse(token);

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        var verified = signedJWT.verify(verifier);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean isNotExpired = expiryTime.after(new Date());

        String jti = signedJWT.getJWTClaimsSet().getJWTID();
        boolean isBlacklisted = refreshTokenService.isAccessTokenBlacklisted(jti);

        return IntrospectResponse.builder()
                .valid(verified && isNotExpired && !isBlacklisted)
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
                .jwtID(UUID.randomUUID().toString())
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


    public AuthResponse refeshToken(String oldRefreshToken) throws JOSEException, ParseException {
        String userIdStr = refreshTokenService.getUsername(oldRefreshToken);

        if (userIdStr == null) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        refreshTokenService.delete(oldRefreshToken);

        UUID userId = UUID.fromString(userIdStr);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = genarateToken(user);
        String newRefreshToken = java.util.UUID.randomUUID().toString();

        refreshTokenService.save(newRefreshToken, user.getId().toString());

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefreshToken)
                .isValid(true)
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getName())
                .roles(user.getRoles().stream()
                        .map(r -> r.getName().name())
                        .collect(Collectors.toSet()))
                .build();
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

        String refreshToken = UUID.randomUUID().toString();

        refreshTokenService.save(refreshToken, user.getId().toString());

        Set<String> roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
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

    public void logOut(String accessToken, String refreshToken) throws ParseException, JOSEException {

        if (refreshToken != null) {
            refreshTokenService.delete(refreshToken);
        }

        SignedJWT signedJWT = SignedJWT.parse(accessToken);
        String jti = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        long ttl = (expiryTime.getTime() - System.currentTimeMillis()) / 1000;

        if (ttl > 0) {
            refreshTokenService.blackListAcessToken(jti, ttl);
        }
    }




}
