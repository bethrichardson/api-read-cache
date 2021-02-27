package com.netflix.repositories.domain.metrics;

import com.netflix.repositories.common.MetricTuple;

import java.util.List;
import java.util.Map;

public interface Metric<T> {

    Map<ViewType, List<MetricTuple>> getViews();

    T getValue();

}
