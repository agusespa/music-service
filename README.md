### Build and Run
Execute Spring Boot's run command at the project's root directory:
``` Bash
$ mvn spring-boot:run
```
The embedded Tomcat server will listen on port 8081.
### Solution breakdown
Simple REST api built with Spring Web and Spring Boot. One controller class with one endpoint, a service layer to process the request, and a global exception handler.  
By default, Spring Boot web applications are multi-threaded and will handle multiple requests concurrently.
#### Response times
The Api calls are blocking but due to the fact that they need to be chained, I can't call them asynchronously.  
We could start a thread for setting the album info and cover art as those operations don't need to wait for the other API calls, but I don't think it'd be worth it since the gains would be marginal.  
In the current state, response times are unacceptable due to an issue covered in 'Known Issues'.  
If the issue is bypassed, the average latency after a load test of 1000 calls is around 550ms. If there is an exception thrown by the external APIs, the error response is 25ms in average.
#### Shortcuts
* Instead of parsing the object returned by the Wikidata Api which would have been complex, I've extracted the data needed directly from the flat string. It shouldn't affect performance and I've written a test for it.
#### Testing impl.
To run the Spring Boot tests, execute Maven's verify command at the project's root directory:
``` Bash
$ mvn clean verify
```
(All tests are currently green.)
* Unit test (mentioned previously).
* System test using WebTestClient and a real Web Environment. Currently, it allows the extreme latency (i.e., it doesn't fail due to timeout). Must be updated once the issue is resolved.
* External load testing with Postman.
### Libraries used
* Spring Web Reactive (WebClient to make the Http calls)
* Jackson for de/serializing
* JUnit 5 and Mockito for unit testing
### Known Issues
* Extremely slow GET requests due to following the redirects of the Cover Art Archive API. Repeated calls add a 10+ second penalty. Tried using RestTemplate and got the same result.
### MBIDs for manually testing the GET details endpoint
GET_URL = http://localhost:8081/musify/music-artist/details/{mbid}
* ba550d0e-adac-4864-b88b-407cab5e76af
* 2fa95b32-3c11-42d7-b495-12fc6be0a024
* 8e66ea2b-b57b-47d9-8df0-df4630aeb8e5
### TODOs
* more granular exception handling with descriptive error messages
* better response for the case in which one external API fails but we have some artist's data to return
* better test coverage
