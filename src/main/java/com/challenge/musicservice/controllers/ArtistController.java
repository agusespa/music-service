package com.challenge.musicservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/musify/music-artist")
public class ArtistController {

    @GetMapping("/details/{mbid}")
    ResponseEntity<String> getArtistDetails(@PathVariable Long id) {
        return ResponseEntity.ok("Successful start up");
    }

}
