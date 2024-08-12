package com.example.demo.controllers;

import com.example.demo.IServices.IGitClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class GitClientController {
    private final IGitClientService gitClientService;

    public GitClientController(IGitClientService gitClientService) {
        this.gitClientService = gitClientService;
    }

    @GetMapping("/repositories")
    public ResponseEntity<?> getNonForkRepositories(@RequestParam String userName){
        return ResponseEntity.ok(gitClientService.getNonForkRepositories(userName));
    }
}
