package com.netflix.apireadcache.metrics.proxied;

import com.netflix.apireadcache.metrics.MetricCollector;
import lombok.AllArgsConstructor;

import java.util.function.Supplier;

@AllArgsConstructor
public class ProxiedMetricCollector implements MetricCollector<Object> {

    Supplier<ProxiedMetric> getMetricProvider;

    @Override
    public ProxiedMetric getMetric() {
        return getMetricProvider.get();
    }

}
