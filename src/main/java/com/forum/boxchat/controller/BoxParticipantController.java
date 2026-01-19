package com.forum.boxchat.controller;

import com.forum.boxchat.dto.request.BoxParticipantDtoRequest;
import com.forum.boxchat.dto.respone.BoxParticipantDtoResponse;
import com.forum.boxchat.service.BoxParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/box-participant")
@RequiredArgsConstructor
public class BoxParticipantController {

    private final BoxParticipantService boxParticipantService;

    @GetMapping("/box/{boxId}")
    public ResponseEntity<List<BoxParticipantDtoResponse>> getByBoxId(
            @PathVariable int boxId
    ) {
        return ResponseEntity.ok(boxParticipantService.findByBoxId(boxId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BoxParticipantDtoResponse>> getByUserId(
            @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(boxParticipantService.findByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<BoxParticipantDtoResponse> addUserToBox(
            @RequestBody BoxParticipantDtoRequest request
    ) {
        return new ResponseEntity<>(
                boxParticipantService.addUserToBox(request),
                HttpStatus.CREATED
        );
    }

    @PostMapping
    public ResponseEntity<BoxParticipantDtoResponse> create(
            @RequestBody BoxParticipantDtoRequest request
    ) {
        return new ResponseEntity<>(
                boxParticipantService.create(request),
                HttpStatus.CREATED
        );
    }


    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeUserFromBox(
            @RequestParam int boxId,
            @RequestParam UUID userId
    ) {
        boxParticipantService.removeUserFromBox(boxId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable Integer id
    ) {
        boxParticipantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
