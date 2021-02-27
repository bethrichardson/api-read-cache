package com.netflix.repositories.domain.metrics.overview;

import com.netflix.repositories.domain.metrics.proxied.ProxiedMetricCache;

public class OverviewMetricCache extends ProxiedMetricCache {

    public OverviewMetricCache(OverviewMetricCollector metricCollector) {
        super(metricCollector);
    }

}
