package com.forum.boxchat.controller;


import com.forum.boxchat.dto.request.UserDtoRequest;
import com.forum.boxchat.dto.respone.UserDtoRespone;
import com.forum.boxchat.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class UserController {

    public final UserService userService;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDtoRespone>> getUsers() {
        var authentication =  SecurityContextHolder.getContext().getAuthentication();
        log.info("Username is {}", authentication.getName());
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PreAuthorize("(hasRole('USER') and hasRole('VERIFIED')) or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDtoRespone> getUser(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping()
    public ResponseEntity<UserDtoRespone> createUser(@Valid @RequestBody UserDtoRequest userDtoRequest) {
        return ResponseEntity.ok(userService.createUser(userDtoRequest));
    }

    @PreAuthorize("(hasRole('USER') and hasRole('VERIFIED')) or hasRole('ADMIN')")
    @PatchMapping()
    public ResponseEntity<UserDtoRespone> updateUser(@Valid @RequestBody UserDtoRequest userDtoRequest) {
        return ResponseEntity.ok(userService.updateUser(userDtoRequest));
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID uuid) {
        userService.deleteUser(uuid);
        return  ResponseEntity.ok().build();
    }

    @GetMapping("/vertify")
    public ResponseEntity<String> vertifyAccount(@RequestParam("token") UUID uuid) {
        return ResponseEntity.ok(userService.verifyAccount(uuid));
    }



}
