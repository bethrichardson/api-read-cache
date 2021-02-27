package com.netflix.repositories.domain.metrics.repositories;

import com.netflix.repositories.domain.metrics.MetricCollector;
import com.netflix.repositories.domain.metrics.caching.MetricsCache;
import com.spotify.github.v3.repos.Repository;

import java.util.List;

public class RepositoryMetricCache extends MetricsCache<List<Repository>> {

    public RepositoryMetricCache(MetricCollector<List<Repository>> metricCollector) {
        super(metricCollector);
    }

}
