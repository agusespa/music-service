package com.challenge.musicservice;

import com.challenge.musicservice.dtos.ArtistDetailsResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
public class ClientTest {

    @Autowired
    WebTestClient client;

    @Test
    public void shouldReturnArtistDetailsResponse() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8081")
                .build();

        client.get().uri("/musify/music-artist/details/f27ec8db-af05-4f36-916e-3d57f91ecf5e")
                .exchange()
                .expectStatus().isOk()
                .expectBody(ArtistDetailsResponse.class);
    }
}
