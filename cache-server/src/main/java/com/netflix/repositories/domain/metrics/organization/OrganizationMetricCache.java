package com.netflix.repositories.domain.metrics.organization;

import com.netflix.repositories.domain.metrics.proxied.ProxiedMetricCache;

public class OrganizationMetricCache extends ProxiedMetricCache {

    public OrganizationMetricCache(OrganizationMetricCollector metricCollector) {
        super(metricCollector);
    }

}
