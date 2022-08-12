### Build and Run
Execute the Spring Boot run command at the project's root directory:
``` Bash
$ mvn spring-boot:run
```
The embedded tomcat server will listen on port 8081.
### Libraries used
* Spring Web Reactive (WebClient to make the Http calls)
* Jackson for de/serializing
* JUnit 5 and Mockito for unit and integration testing
### Known Issues
* Extremely slow GET requests due to following redirects of the Cover Art Archive API.
### MBIDs for manually testing the GET details endpoint
GET_URL = http://localhost:8081/musify/music-artist/details/{mbid}
* ba550d0e-adac-4864-b88b-407cab5e76af
* 2fa95b32-3c11-42d7-b495-12fc6be0a024
* 8e66ea2b-b57b-47d9-8df0-df4630aeb8e5
### TODOs
* more granular exception handling
* better responses for the case in which one external API fails but we have some data to return to the client
* better test coverage
