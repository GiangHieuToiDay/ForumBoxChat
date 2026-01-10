package com.forum.boxchat.controller;


import com.forum.boxchat.dto.request.LoginRequest;
import com.forum.boxchat.dto.respone.AuthResponse;
import com.forum.boxchat.sercurity.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    public final AuthenticationService authenticationService;

    @PostMapping()
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body( authenticationService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword()) );
    }

    @PostMapping("/change-password")
    public void changePassword(@Valid @RequestBody LoginRequest loginRequest) {
        authenticationService.changePassword(loginRequest.getEmail(), loginRequest.getPassword());
    }





}
