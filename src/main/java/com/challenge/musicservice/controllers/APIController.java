package com.challenge.musicservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArtistController {

    @GetMapping("/api/status")
    ResponseEntity<String> getAPIStatus() {
        return ResponseEntity.ok("Successful start up");
    }

}
