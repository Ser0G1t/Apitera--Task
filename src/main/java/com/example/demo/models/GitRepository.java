package com.example.demo.models;

import java.util.List;

public record GitRepository (String name, GitOwner owner, boolean fork, List<GitBranch> branches) {
    public boolean isNotFork(){
        return !fork;
    }
}

