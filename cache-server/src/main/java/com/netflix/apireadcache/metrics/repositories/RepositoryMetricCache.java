package com.netflix.apireadcache.metrics.repositories;

import com.netflix.apireadcache.metrics.MetricCollector;
import com.netflix.apireadcache.metrics.caching.CachingStrategy;
import com.netflix.apireadcache.metrics.caching.MetricsCache;
import com.spotify.github.v3.repos.Repository;

import java.util.List;

public class RepositoryMetricCache extends MetricsCache<List<Repository>> {

    public RepositoryMetricCache(MetricCollector<List<Repository>> metricCollector, CachingStrategy cachingStrategy) {
        super(metricCollector, cachingStrategy);
    }

}
