package com.netflix.apireadcache.metrics.proxied;

import com.netflix.apireadcache.metrics.MetricCollector;
import com.netflix.apireadcache.metrics.MetricType;
import com.netflix.apireadcache.metrics.ViewType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

@AllArgsConstructor
public class ProxiedMetricCollector implements MetricCollector<Object> {

    private static final ViewType[] NO_SUPPORTED_VIEWS = {};
    @Getter
    private MetricType metricType;
    Supplier<ProxiedMetric> getMetricProvider;

    @Override
    public ProxiedMetric getMetric() {
        return getMetricProvider.get();
    }

    @Override
    public ViewType[] getSupportedViews() {
        return NO_SUPPORTED_VIEWS;
    }

}
