package com.netflix.repositories.domain.metrics.members;

import com.netflix.repositories.domain.metrics.proxied.ProxiedMetricCache;

public class MembersMetricCache extends ProxiedMetricCache {

    public MembersMetricCache(MembersMetricCollector metricCollector) {
        super(metricCollector);
    }

}
