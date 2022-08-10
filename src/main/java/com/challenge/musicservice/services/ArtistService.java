package com.challenge.musicservice.services;

import com.challenge.musicservice.dtos.ArtistDetailsResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ArtistService {

	WebClient.Builder webclient;

	public ArtistService(WebClient.Builder webclient) {
		this.webclient = webclient;
	}

	public ArtistDetailsResponse getArtistDetails(String mbid) {
		return new ArtistDetailsResponse(mbid,"Chad");
	}
}
