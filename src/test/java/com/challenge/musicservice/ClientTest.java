package com.challenge.musicservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ClientTest {

    @Autowired
    WebTestClient client;

    @Test
    public void shouldReturnArtistDetailsResponse() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8081")
                .responseTimeout(Duration.ofMillis(10000000)) // temporary, see issues in README
                .build();

        client.get().uri("/musify/music-artist/details/f27ec8db-af05-4f36-916e-3d57f91ecf5e")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.name").isEqualTo("Michael Jackson");
    }
}
