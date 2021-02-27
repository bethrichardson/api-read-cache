package com.netflix.apireadcache.metrics.proxied;

import com.netflix.apireadcache.metrics.Metric;
import com.netflix.apireadcache.metrics.MetricTuple;
import com.netflix.apireadcache.metrics.ViewType;
import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
public class ProxiedMetric implements Metric<Object> {

    private final Object value;

    @Override
    public Map<ViewType, List<MetricTuple>> getViews() {
        return Collections.emptyMap();
    }

    @Override
    public Object getValue() {
        return value;
    }

}
