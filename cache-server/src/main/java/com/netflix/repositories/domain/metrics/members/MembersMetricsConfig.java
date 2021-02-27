package com.netflix.repositories.domain.metrics.members;

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
public class MembersMetricsConfig {

    @Bean
    MembersMetricCollector membersMetricCollector(CachingGitHubClient client, GithubCredentials credentials) {
        return new MembersMetricCollector(client, credentials.getOrganization());
    }

    @Bean
    public MembersMetricCache metricsCache(MembersMetricCollector metricCollector) {
        MembersMetricCache cache = new MembersMetricCache(metricCollector);
        cache.initializeCache();
        return cache;
    }

}
