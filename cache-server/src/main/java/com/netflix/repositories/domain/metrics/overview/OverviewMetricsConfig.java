package com.netflix.repositories.domain.metrics.overview;

import com.netflix.repositories.domain.metrics.github.CachingGitHubClient;
import com.netflix.repositories.domain.metrics.github.GithubConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        GithubConfig.class
})
public class OverviewMetricsConfig {

    @Bean
    OverviewMetricCollector overviewMetricCollector(CachingGitHubClient client) {
        return new OverviewMetricCollector(client);
    }

    @Bean
    public OverviewMetricCache overviewMetricCache(OverviewMetricCollector metricCollector) {
        OverviewMetricCache cache = new OverviewMetricCache(metricCollector);
        cache.initializeCache();
        return cache;
    }

}
