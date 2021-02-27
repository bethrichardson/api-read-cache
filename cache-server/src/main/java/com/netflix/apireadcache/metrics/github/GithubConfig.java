package com.netflix.apireadcache.metrics.github;

import com.netflix.apireadcache.client.ResourcePaths;
import com.netflix.apireadcache.config.JsonOrTextDecoder;
import feign.Feign;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GithubConfig {

    public static final String NETFLIX = "Netflix";

    @Getter
    @Value("${GITHUB_API_TOKEN}")
    private transient String apiToken;

    @Bean
    public GithubCredentials githubCredentials() {
        return GithubCredentials.builder()
                .apiToken(apiToken)
                .build();
    }

    @Bean
    public ProxiedGitHubClient cachingGithubClient() {
        return Feign.builder()
                .decoder(new JsonOrTextDecoder())
                .target(ProxiedGitHubClient.class, ResourcePaths.GIT_HUB_API);
    }

}
