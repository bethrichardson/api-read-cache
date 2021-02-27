package com.netflix.repositories.domain.metrics.proxied;

import com.netflix.repositories.common.MetricTuple;
import com.netflix.repositories.domain.metrics.Metric;
import com.netflix.repositories.domain.metrics.ViewType;
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
