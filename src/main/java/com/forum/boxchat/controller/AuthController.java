package com.forum.boxchat.controller;


import com.forum.boxchat.dto.request.LoginRequest;
import com.forum.boxchat.dto.request.LogoutRequest;
import com.forum.boxchat.dto.request.RefreshRequest;
import com.forum.boxchat.dto.respone.AuthResponse;
import com.forum.boxchat.sercurity.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    public final AuthenticationService authenticationService;

    @PostMapping()
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        var authentication =  SecurityContextHolder.getContext().getAuthentication();

        log.info("Username is {}", authentication.getName());
        return ResponseEntity.ok().body( authenticationService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword()) );
    }

    @PostMapping("/change-password")
    public void changePassword(@Valid @RequestBody LoginRequest loginRequest) {
        authenticationService.changePassword(loginRequest.getEmail(), loginRequest.getPassword());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request)
            throws JOSEException, ParseException {

        var result = authenticationService.refeshToken(request.getToken());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/log-out")
    public ResponseEntity<Void> logOut(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody LogoutRequest request
    ) throws ParseException, JOSEException {

        String accessToken = authHeader.substring(7);
        authenticationService.logOut(accessToken, request.getRefreshToken());

        return ResponseEntity.noContent().build();
    }




}
