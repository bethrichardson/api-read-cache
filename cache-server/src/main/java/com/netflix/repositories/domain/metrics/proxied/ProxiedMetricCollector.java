package com.netflix.repositories.domain.metrics.proxied;

import com.netflix.repositories.domain.metrics.MetricCollector;
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
