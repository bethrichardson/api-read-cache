package com.netflix.repositories.domain.metrics;

import com.netflix.repositories.common.RepositoryMetric;
import com.netflix.repositories.domain.metrics.caching.MetricsCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MetricsService {

    @Autowired
    private MetricsCache cache;

    public List<RepositoryMetric> getForkMetrics(int numRepos) {
        List<RepositoryMetric> metrics = cache.getMetric(MetricType.FORKS);
        if (metrics.size() > numRepos) {
            return metrics.subList(0, numRepos);
        }
        return metrics;
    }



}
