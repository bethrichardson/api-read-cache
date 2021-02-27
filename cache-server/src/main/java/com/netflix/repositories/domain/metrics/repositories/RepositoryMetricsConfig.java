package com.netflix.repositories.domain.metrics.repositories;

import com.netflix.repositories.client.ResourcePaths;
import com.netflix.repositories.domain.metrics.github.GithubConfig;
import com.netflix.repositories.domain.metrics.github.GithubCredentials;
import com.netflix.repositories.domain.metrics.ViewType;
import com.spotify.github.v3.clients.GitHubClient;
import com.spotify.github.v3.clients.RepositoryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.net.URI;

@Configuration
@Import({
        GithubConfig.class
})
public class RepositoryMetricsConfig {

    @Bean
    GitHubClient spotifyGithubClient(GithubCredentials credentials) {
        return GitHubClient.create(URI.create(ResourcePaths.GIT_HUB_API), credentials.getApiToken());
    }

    @Bean
    RepositoryClient repositoryClient(GitHubClient gitHubClient, GithubCredentials credentials) {
        return gitHubClient.createRepositoryClient(credentials.getOrganization(), null);
    }

    @Bean
    RepositoryMetricCollector repositoryMetricCollector(RepositoryClient repositoryClient) {
        return new RepositoryMetricCollector(repositoryClient);
    }

    @Bean
    public RepositoryMetricCache repositoryMetricCache(RepositoryMetricCollector repositoryMetricsCollector) {
        RepositoryMetricCache cache = new RepositoryMetricCache(repositoryMetricsCollector);
        cache.initializeCache(supportedViews());
        return cache;
    }

    protected ViewType[] supportedViews() {
        return ViewType.values();
    }

}
