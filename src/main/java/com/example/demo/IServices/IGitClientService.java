package com.example.demo.IServices;

import com.example.demo.models.GitRepository;
import reactor.core.publisher.Flux;

public interface IGitClientService {
    Flux<GitRepository> getNonForkRepositories(String username);
}
