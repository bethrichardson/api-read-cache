package com.netflix.repositories;

import com.netflix.repositories.config.RepositoryMetricsConfig;
import com.spotify.github.v3.clients.GitHubClient;
import com.spotify.github.v3.clients.RepositoryClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import spock.mock.DetachedMockFactory;

@SuppressWarnings("PMD.BeanMembersShouldSerialize")
@Configuration
@EnableAutoConfiguration
@Import({
        RepositoryMetrics.class,
        RepositoryMetricsConfig.class
})
public class ComponentTestConfig {

    private final DetachedMockFactory mockFactory = new DetachedMockFactory();

    @Bean
    @Primary
    public GitHubClient mockGitHubClient() {
        return mockFactory.Mock(GitHubClient.class);
    }

    @Bean
    @Primary
    public RepositoryClient mockRepoClient() {
        return mockFactory.Mock(RepositoryClient.class);
    }

}
