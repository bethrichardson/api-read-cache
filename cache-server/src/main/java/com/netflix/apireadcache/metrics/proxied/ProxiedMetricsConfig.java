package com.netflix.apireadcache.metrics.proxied;

import com.netflix.apireadcache.metrics.github.ProxiedGitHubClient;
import com.netflix.apireadcache.metrics.github.GithubConfig;
import com.netflix.apireadcache.metrics.github.GithubCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        GithubConfig.class
})
public class ProxiedMetricsConfig {

    @Bean
    public ProxiedMetricCache overviewMetricCache(ProxiedGitHubClient client) {
        ProxiedMetricCollector collector = new ProxiedMetricCollector(()-> new ProxiedMetric(client.getOverview()));
        ProxiedMetricCache cache = new ProxiedMetricCache(collector);
        cache.initializeCache();
        return cache;
    }

    @Bean
    public ProxiedMetricCache organizationMetricCache(ProxiedGitHubClient client, GithubCredentials credentials) {
        ProxiedMetricCollector collector = new ProxiedMetricCollector(()-> new ProxiedMetric(client.getOrganization(credentials.getOrganization())));
        ProxiedMetricCache cache = new ProxiedMetricCache(collector);
        cache.initializeCache();
        return cache;
    }

    @Bean
    public ProxiedMetricCache membersMetricCache(ProxiedGitHubClient client, GithubCredentials credentials) {
        ProxiedMetricCollector collector = new ProxiedMetricCollector(()-> new ProxiedMetric(client.getOrganizationMembers(credentials.getOrganization())));
        ProxiedMetricCache cache = new ProxiedMetricCache(collector);
        cache.initializeCache();
        return cache;
    }

}
