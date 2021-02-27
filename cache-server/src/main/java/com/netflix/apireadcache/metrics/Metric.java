package com.netflix.apireadcache.metrics;

import java.util.List;
import java.util.Map;

public interface Metric<T> {

    Map<ViewType, List<MetricTuple>> getViews();

    T getValue();

}
