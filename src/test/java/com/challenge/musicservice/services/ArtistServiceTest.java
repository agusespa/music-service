package com.challenge.musicservice.services;

import com.challenge.musicservice.pojos.MBArtist;
import com.challenge.musicservice.pojos.Relation;
import com.challenge.musicservice.pojos.RelationUrl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArtistServiceTest {

    @InjectMocks
    ArtistService artistService;

    MBArtist artist = new MBArtist();

    @BeforeAll
    public void setUp() {
        artist.setName("Michael Jackson");
        artist.setGender("Male");
        artist.setCountry("US");
        artist.setDisambiguation("“King of Pop”");
        Relation relation = new Relation();
        relation.setType("wikidata");
        RelationUrl relationUrl = new RelationUrl();
        relationUrl.setResource("https://www.wikidata.org/wiki/Q2831");
        relation.setUrl(relationUrl);
        List<Relation> list = new ArrayList<>();
        list.add(relation);
        artist.setRelations(list);
    }

    @Test
    public void shouldReturnWikidataLink() {
        String expectedUrl = "https://www.wikidata.org/wiki/Special:EntityData/Q2831.json";
        String testUrl = artistService.buildWikidataLink(artist);

        assertEquals(expectedUrl, testUrl);
    }

    @Test
    public void shouldReturnWikipediaTitle() throws IOException {
        SampleWikidataJson sampleWikidataJson = new SampleWikidataJson();

        String expectedTitle = "Michael_Jackson";
        String testUrl = artistService.findWikipediaTitle(sampleWikidataJson.getJsonStr());

        assertEquals(expectedTitle, testUrl);
    }
}
