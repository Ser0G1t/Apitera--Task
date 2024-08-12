package com.example.demo;

import com.example.demo.models.GitRepository;
import com.example.demo.services.GitClientService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;


import java.io.IOException;


public class GitClientServiceTest {
    private static MockWebServer mockWebServer;
    private static GitClientService gitClientService;

    @BeforeEach
    public void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        WebClient.Builder webClientBuilder = WebClient.builder().baseUrl(mockWebServer.url("/").toString());
        gitClientService = new GitClientService(webClientBuilder);
    }

    @AfterEach
    public void tearDown() throws IOException {
        mockWebServer.close();
    }
    @Test
    public void getNonForkRepositories_ShouldReturn_Correctly_WhenUsernameIsGiven() throws IOException {
        //given
        String ownerName="Ser0G1t";
        //when
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.OK.value())
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(getJson()));

        Flux<GitRepository> result = gitClientService.getNonForkRepositories(ownerName);
        //then
       var test=StepVerifier.create(result)
               .expectNextMatches(el->el.owner().login().equals(ownerName))
               .thenConsumeWhile(t -> true)
               .verifyComplete();
    }
    @Test
    public void getNonForkRepositories_ShouldReturn_FalsedScenario_WhenUsernameIsGiven() throws IOException {
        //given
        String ownerName="Ser0G1tnothere";
        //when
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(HttpStatus.NOT_FOUND.value()));

        Flux<GitRepository> result = gitClientService.getNonForkRepositories(ownerName);
        //then
        var test=StepVerifier.create(result)
                .expectError(WebClientResponseException.NotFound.class)
                .verify();
    }
    private String getJson () {
        return "[{\"name\":\"Food-Diary\",\"owner\":{\"login\":\"Ser0G1t\"},\"fork\":false,\"branches\":[{\"name\":\"main\",\"commit\":{\"sha\":\"04229fcea1f8cfb66c002e3b86d9e5acc9cd4be2\"}}],\"notFork\":true}]";
    }


}
