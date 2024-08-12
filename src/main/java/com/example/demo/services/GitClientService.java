package com.example.demo.services;

import com.example.demo.IServices.IGitClientService;
import com.example.demo.models.GitBranch;
import com.example.demo.models.GitRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GitClientService implements IGitClientService {
    private static final String MAIN_URL = "https://api.github.com";
    private final WebClient webClient;

    public GitClientService(WebClient.Builder webClient) {
        this.webClient = webClient
                .baseUrl(MAIN_URL)
                .defaultHeader("Accept", "application/json")
                .build();
    }
    public Flux<GitRepository> getNonForkRepositories(String username) {
        return webClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToFlux(GitRepository.class)
                .filter(GitRepository::isNotFork)
                .flatMap(this::getBranches);
    }
    private Mono<GitRepository> getBranches(GitRepository gitRepository) {
        return webClient.get()
                .uri("/repos/{user}/{repo}/branches", gitRepository.owner().login(), gitRepository.name())
                .retrieve()
                .bodyToFlux(GitBranch.class)
                .collectList()
                .map(branches -> new GitRepository(gitRepository.name(), gitRepository.owner(), gitRepository.fork(), branches));
    }
}
