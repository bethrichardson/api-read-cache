package com.netflix.repositories.domain.metrics.caching;

import com.netflix.repositories.domain.github.GithubConfig;
import com.netflix.repositories.domain.github.GithubMetricsCollector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        GithubConfig.class
})
public class CachingConfig {

    @Autowired
    GithubMetricsCollector githubMetricsCollector;

    public TimeProvider timeProvider() {
        return new TimeProvider();
    }

    @Bean
    public MetricsCache metricsCache() {
        MetricsCache cache = new MetricsCache(githubMetricsCollector, timeProvider());
        cache.initializeCache();
        return cache;
    }
}
