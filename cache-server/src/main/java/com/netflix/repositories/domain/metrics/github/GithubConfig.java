package com.netflix.repositories.domain.metrics.github;

import com.netflix.repositories.client.ResourcePaths;
import com.netflix.repositories.config.JsonOrTextDecoder;
import feign.Feign;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GithubConfig {

    @Getter
    @Value("${GITHUB_API_TOKEN}")
    private transient String apiToken;

    @Getter
    @Value("${github.organization:Netflix}")
    private transient String organization;

    @Bean
    public GithubCredentials githubCredentials() {
        return GithubCredentials.builder()
                .apiToken(apiToken)
                .organization(organization)
                .build();
    }

    @Bean
    public CachingGitHubClient cachingGithubClient() {
        return Feign.builder()
                .decoder(new JsonOrTextDecoder())
                .target(CachingGitHubClient.class, ResourcePaths.GIT_HUB_API);
    }

}
