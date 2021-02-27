package com.netflix.apireadcache.metrics.repositories;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.apireadcache.client.ResourcePaths;
import com.netflix.apireadcache.metrics.github.GithubConfig;
import com.netflix.apireadcache.metrics.github.GithubCredentials;
import com.netflix.apireadcache.metrics.ViewType;
import com.spotify.github.v3.clients.GitHubClient;
import com.spotify.github.v3.clients.RepositoryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.net.URI;

import static com.netflix.apireadcache.metrics.github.GithubConfig.NETFLIX;

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
    RepositoryClient repositoryClient(GitHubClient gitHubClient) {
        return gitHubClient.createRepositoryClient(NETFLIX, null);
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

    /**
     * Visibility set to ANY to allow serializing Repository objects
     * with com.spotify.github.GitHubInstant
     */
    @Bean
    ObjectMapper githubObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return objectMapper;
    }

}
