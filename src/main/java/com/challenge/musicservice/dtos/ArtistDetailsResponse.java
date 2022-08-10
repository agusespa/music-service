package com.challenge.musicservice.dtos;

public class ArtistDetailsResponse {

	private String mbid;

	private String name;

	public ArtistDetailsResponse(String mbid, String name) {
		this.mbid = mbid;
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
