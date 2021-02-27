package com.netflix.repositories.domain.metrics.proxied;

import com.netflix.repositories.domain.metrics.MetricCollector;
import com.netflix.repositories.domain.metrics.ViewType;
import com.netflix.repositories.domain.metrics.caching.MetricsCache;

public class ProxiedMetricCache extends MetricsCache<Object> {

    private static final ViewType[] NO_SUPPORTED_VIEWS = {};

    public ProxiedMetricCache(MetricCollector<Object> metricCollector) {
        super(metricCollector);
    }

    public void initializeCache() {
        initializeCache(NO_SUPPORTED_VIEWS);
    }

}
