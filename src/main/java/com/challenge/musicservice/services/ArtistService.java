package com.challenge.musicservice.services;

import com.challenge.musicservice.dtos.Album;
import com.challenge.musicservice.dtos.ArtistDetailsResponse;

import com.challenge.musicservice.pojos.MBArtist;
import com.challenge.musicservice.pojos.Relation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

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

//		setArtistAlbums(artistDetailsResponse.getAlbums(), mbArtist.getReleaseGroups());
	}

	private void setArtistAlbums(List<Album> albums, Map<String, Object> map) {
		List list = (List) map.get("release-group");

		for (Object m : list) {
			Map<String, String> map1 = (Map) m;
			String albumUrl = getMusicBrainzAlbumUrl(map1.get("id"));
			albums.add(new Album(map1.get("id"), map1.get("title"), albumUrl));
		}
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

	private String getWikidataLink(MBArtist artist) {
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

	private String getMusicBrainzAlbumUrl(String id) {
		String baseUrl = "http://coverartarchive.org/release-group/";

		// API redirects
		try {
			String jsonMap = webClientBuilder.build()
					.get()
					.uri(baseUrl + id)
					.retrieve()
					.bodyToMono(String.class)
					.block();

			return jsonMap;

		} catch (Exception e) {
			throw e;
		}
	}
}
