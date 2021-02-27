package com.netflix.apireadcache.metrics.proxied;

import com.netflix.apireadcache.metrics.github.GithubConfig;
import com.netflix.apireadcache.metrics.github.ProxiedGitHubClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static com.netflix.apireadcache.metrics.github.GithubConfig.NETFLIX;

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
    public ProxiedMetricCache organizationMetricCache(ProxiedGitHubClient client) {
        ProxiedMetricCollector collector = new ProxiedMetricCollector(()-> new ProxiedMetric(client.getOrganization(NETFLIX)));
        ProxiedMetricCache cache = new ProxiedMetricCache(collector);
        cache.initializeCache();
        return cache;
    }

    @Bean
    public ProxiedMetricCache membersMetricCache(ProxiedGitHubClient client) {
        ProxiedMetricCollector collector = new ProxiedMetricCollector(()-> new ProxiedMetric(client.getOrganizationMembers(NETFLIX)));
        ProxiedMetricCache cache = new ProxiedMetricCache(collector);
        cache.initializeCache();
        return cache;
    }

}
