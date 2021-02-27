package com.netflix.repositories.domain.metrics.organization;

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
public class OrganizationMetricsConfig {

    @Bean
    OrganizationMetricCollector organizationMetricCollector(CachingGitHubClient client, GithubCredentials credentials) {
        return new OrganizationMetricCollector(client, credentials.getOrganization());
    }

    @Bean
    public OrganizationMetricCache organizationMetricCache(OrganizationMetricCollector metricCollector) {
        OrganizationMetricCache cache = new OrganizationMetricCache(metricCollector);
        cache.initializeCache();
        return cache;
    }

}
