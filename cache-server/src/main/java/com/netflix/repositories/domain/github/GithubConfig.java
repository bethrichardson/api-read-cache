package com.netflix.repositories.domain.github;

import com.spotify.github.v3.clients.GitHubClient;
import com.spotify.github.v3.clients.RepositoryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class GithubConfig {

    @Value("${GITHUB_API_TOKEN}")
    private transient String apiToken;

    @Value("${github.organization:Netflix}")
    private transient String githubOrganization;


    @Bean
    GitHubClient gitHubClient() {
        return GitHubClient.create(URI.create("https://api.github.com"), apiToken);
    }

    @Bean
    RepositoryClient repositoryClient() {
        return gitHubClient().createRepositoryClient(githubOrganization, null);
    }

    @Bean
    GithubService githubService() {
        return new GithubService(repositoryClient());
    }

}
