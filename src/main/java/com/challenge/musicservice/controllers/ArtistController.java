package com.challenge.musicservice.controllers;

import com.challenge.musicservice.services.ArtistService;
import com.challenge.musicservice.dtos.ArtistDetailsResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/musify/music-artist")
public class ArtistController {

    private final ArtistService artistService;

    public ArtistController(ArtistService service) {
        this.artistService = service;
    }

    @GetMapping("/details/{mbid}")
    ResponseEntity<ArtistDetailsResponse> getArtistDetails(@PathVariable String mbid) {
        return ResponseEntity.ok(artistService.getArtistDetails(mbid));
    }
}
