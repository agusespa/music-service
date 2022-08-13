package com.challenge.musicservice.dtos;

import com.challenge.musicservice.pojos.Album;

import java.util.ArrayList;
import java.util.List;

public class ArtistDetailsResponse {

	private String mbid;

	private String name;

	private String gender;

	private String country;

	private String disambiguation;

	private String description;

	private List<Album> albums;

	public ArtistDetailsResponse() {}

	public ArtistDetailsResponse(String mbid) {
		this.mbid = mbid;
		this.albums = new ArrayList<>();
	}

	public String getMbid() {
		return mbid;
	}

	public void setMbid(String mbid) {
		this.mbid = mbid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDisambiguation() {
		return disambiguation;
	}

	public void setDisambiguation(String disambiguation) {
		this.disambiguation = disambiguation;
	}

	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}
}
