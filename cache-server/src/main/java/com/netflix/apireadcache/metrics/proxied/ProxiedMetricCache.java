package com.netflix.apireadcache.metrics.proxied;

import com.netflix.apireadcache.metrics.MetricCollector;
import com.netflix.apireadcache.metrics.caching.CachingStrategy;
import com.netflix.apireadcache.metrics.caching.MetricsCache;

public class ProxiedMetricCache extends MetricsCache<Object> {

    public ProxiedMetricCache(MetricCollector<Object> metricCollector, CachingStrategy cachingStrategy) {
        super(metricCollector, cachingStrategy);
    }

}
