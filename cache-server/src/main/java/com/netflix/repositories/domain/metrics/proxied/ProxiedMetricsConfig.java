package com.netflix.repositories.domain.metrics.proxied;

import com.netflix.repositories.domain.metrics.github.CachingGitHubClient;
import com.netflix.repositories.domain.metrics.github.GithubConfig;
import com.netflix.repositories.domain.metrics.github.GithubCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        GithubConfig.class
})
public class ProxiedMetricsConfig {

    @Bean
    public ProxiedMetricCache overviewMetricCache(CachingGitHubClient client) {
        ProxiedMetricCollector collector = new ProxiedMetricCollector(()-> new ProxiedMetric(client.getOverview()));
        ProxiedMetricCache cache = new ProxiedMetricCache(collector);
        cache.initializeCache();
        return cache;
    }

    @Bean
    public ProxiedMetricCache organizationMetricCache(CachingGitHubClient client, GithubCredentials credentials) {
        ProxiedMetricCollector collector = new ProxiedMetricCollector(()-> new ProxiedMetric(client.getOrganization(credentials.getOrganization())));
        ProxiedMetricCache cache = new ProxiedMetricCache(collector);
        cache.initializeCache();
        return cache;
    }

    @Bean
    public ProxiedMetricCache membersMetricCache(CachingGitHubClient client, GithubCredentials credentials) {
        ProxiedMetricCollector collector = new ProxiedMetricCollector(()-> new ProxiedMetric(client.getOrganizationMembers(credentials.getOrganization())));
        ProxiedMetricCache cache = new ProxiedMetricCache(collector);
        cache.initializeCache();
        return cache;
    }

}
