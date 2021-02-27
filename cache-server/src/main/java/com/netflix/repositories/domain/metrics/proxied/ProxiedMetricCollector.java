package com.netflix.repositories.domain.metrics.proxied;

import com.netflix.repositories.domain.metrics.MetricCollector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ProxiedMetricCollector implements MetricCollector<Object> {

    @Override
    public abstract ProxiedMetric getMetric();

}
