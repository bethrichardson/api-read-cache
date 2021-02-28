package com.netflix.apireadcache.metrics.github;

import com.netflix.apireadcache.config.JsonOrTextDecoder;
import com.spotify.github.v3.clients.GitHubClient;
import feign.Feign;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class GithubConfig {

    public static final String NETFLIX = "Netflix";

    @Getter
    @Value("${GITHUB_API_TOKEN}")
    private transient String apiToken;

    @Getter
    @Value("${github.api.url}")
    private transient String apiUrl;

    @Bean
    public GithubCredentials githubCredentials() {
        return GithubCredentials.builder()
                .apiToken(apiToken)
                .apiUrl(apiUrl)
                .build();
    }

    @Bean
    public ProxiedGitHubClient cachingGithubClient() {
        return Feign.builder()
                .decoder(new JsonOrTextDecoder())
                .target(ProxiedGitHubClient.class, apiUrl);
    }

    @Bean
    GitHubClient spotifyGithubClient(GithubCredentials credentials) {
        return GitHubClient.create(URI.create(apiUrl), credentials.getApiToken());
    }

}
