package com.netflix.apireadcache.metrics.proxied;

import com.netflix.apireadcache.metrics.MetricCollector;
import com.netflix.apireadcache.metrics.ViewType;
import com.netflix.apireadcache.metrics.caching.MetricsCache;

public class ProxiedMetricCache extends MetricsCache<Object> {

    private static final ViewType[] NO_SUPPORTED_VIEWS = {};

    public ProxiedMetricCache(MetricCollector<Object> metricCollector) {
        super(metricCollector);
    }

    public void initializeCache() {
        initializeCache(NO_SUPPORTED_VIEWS);
    }

}
