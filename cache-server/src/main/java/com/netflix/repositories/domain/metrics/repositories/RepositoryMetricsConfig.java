package com.netflix.repositories.domain.metrics.repositories;

import com.netflix.repositories.domain.metrics.ViewType;
import com.spotify.github.v3.clients.GitHubClient;
import com.spotify.github.v3.clients.RepositoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class RepositoryMetricsConfig {

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
    RepositoryMetricCollector githubService(RepositoryClient repositoryClient) {
        return new RepositoryMetricCollector(repositoryClient);
    }

    @Autowired
    RepositoryMetricCollector repositoryMetricsCollector;

    @Bean
    public RepositoryMetricCache metricsCache() {
        RepositoryMetricCache cache = new RepositoryMetricCache(repositoryMetricsCollector);
        cache.initializeCache(supportedViews());
        return cache;
    }
    
    protected ViewType[] supportedViews() {
        return ViewType.values();
    }

}
