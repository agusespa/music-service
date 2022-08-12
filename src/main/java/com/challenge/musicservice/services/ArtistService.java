package com.challenge.musicservice.services;

import com.challenge.musicservice.pojos.Album;
import com.challenge.musicservice.dtos.ArtistDetailsResponse;

import com.challenge.musicservice.exceptions.ImageUrlNotFoundException;
import com.challenge.musicservice.pojos.CoverArt;
import com.challenge.musicservice.pojos.MBArtist;
import com.challenge.musicservice.pojos.Relation;
import com.challenge.musicservice.pojos.ReleaseGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ArtistService {

	WebClient.Builder webClientBuilder;

	Logger logger = LoggerFactory.getLogger(ArtistService.class);

	public ArtistService(WebClient.Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
	}

	public ArtistDetailsResponse getArtistDetails(String mbid) {
		ArtistDetailsResponse artistDetailsResponse = new ArtistDetailsResponse(mbid);

		setArtistInfo(artistDetailsResponse);

		return artistDetailsResponse;
	}

	private void setArtistInfo(ArtistDetailsResponse artistDetailsResponse) {

		MBArtist mbArtist = getMusicBrainzArtistData(artistDetailsResponse.getMbid());

		artistDetailsResponse.setName(mbArtist.getName());
		artistDetailsResponse.setGender(mbArtist.getGender());
		artistDetailsResponse.setCountry(mbArtist.getCountry());
		artistDetailsResponse.setDisambiguation(mbArtist.getDisambiguation());

		String description = getWikipediaArtistData(mbArtist);
		artistDetailsResponse.setDescription(description);

		artistDetailsResponse.setAlbums(getArtistAlbums(mbArtist));
	}

	private MBArtist getMusicBrainzArtistData(String mbid) {
		String baseURL = "http://musicbrainz.org/ws/2/artist/";
		String URLparams = "?&fmt=json&inc=url-rels+release-groups";

		try {
			MBArtist mbArtist = webClientBuilder.build()
					.get()
					.uri(baseURL + mbid + URLparams)
					.retrieve()
					.bodyToMono(MBArtist.class)
					.block();

			return mbArtist;

		} catch (Exception e) {
			throw e;
		}
	}

	private String getWikipediaArtistData(MBArtist artist) {
		String baseURL = "https://en.wikipedia.org/api/rest_v1/page/summary";
		String title = getWikipediaTitle(artist);

		try {
			Map jsonMap = webClientBuilder.build()
					.get()
					.uri(baseURL + title)
					.retrieve()
					.bodyToMono(Map.class)
					.block();
			return (String) jsonMap.get("extract");
		} catch (Exception e) {
			throw e;
		}

	}

	private String getWikipediaTitle(MBArtist artist) {

		String wikidataLink = getWikidataLink(artist);

		try {
			String jsonStr = webClientBuilder
					.exchangeStrategies(ExchangeStrategies.builder()
							.codecs(configurer -> configurer
									.defaultCodecs()
									.maxInMemorySize(16 * 1024 * 1024))
							.build())
					.build()
					.get()
					.uri(wikidataLink)
					.retrieve()
					.bodyToMono(String.class)
					.block();

			int firstIndex = jsonStr.indexOf("enwiki");
			String tempSubstring = jsonStr.substring(firstIndex);
			int endIndex = tempSubstring.indexOf('}')-1; // -1 to account for '"'
			int startIndex = 0;
			for (int i = endIndex; i > 0; i--) {
				if (tempSubstring.charAt(i) == '/') {
					startIndex = i;
					break;
				}
			}
			String title = tempSubstring.substring(startIndex, endIndex);

			return title;

		} catch (Exception e) {
			throw e;
		}
	}

	 String getWikidataLink(MBArtist artist) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("https://www.wikidata.org/wiki/Special:EntityData");

		StringBuilder link = new StringBuilder();
		for (Relation relation : artist.getRelations()) {
			if (relation.getType().equals("wikidata")) {
				link.append(relation.getUrl().getResource());
				break;
			}
		}

		// find substring with the code to build wikidata link
		int startIndex = 0;
		for (int i = link.length()-1; i > 0; i--) {
			if (link.charAt(i) == '/') {
				startIndex = i;
				break;
			}
		}
		String code = link.substring(startIndex);

		stringBuilder.append(code);
		stringBuilder.append(".json");

		return stringBuilder.toString();
	}

	private List<Album> getArtistAlbums(MBArtist artist) {

		List<Album> albums = new ArrayList<>();

		for (ReleaseGroup releaseGroup : artist.getReleaseGroups()) {
			Album album = new Album();
			album.setId(releaseGroup.getId());
			album.setTitle(releaseGroup.getTitle());
			album.setImageUrl(getMusicBrainzAlbumUrl(releaseGroup.getId()));
			albums.add(album);
		}

		return albums;
	}

	private String getMusicBrainzAlbumUrl(String id) {
		String baseUrl = "http://coverartarchive.org/release-group/";

		try {
			CoverArt coverData = webClientBuilder
					.clientConnector(new ReactorClientHttpConnector(HttpClient.create().followRedirect(true)))
					.build()
					.get()
					.uri(baseUrl + id)
					.retrieve()
					.onStatus(HttpStatus::is4xxClientError, response -> {
						throw new ImageUrlNotFoundException();
					})
					.bodyToMono(CoverArt.class)
					.block();

			return coverData.getImages().get(0).getImageUrl();

		} catch (ImageUrlNotFoundException e) {
			return "unavailable";
		} catch (Exception e) {
			throw e;
		}
	}
}
