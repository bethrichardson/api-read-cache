package com.netflix.apireadcache.metrics;

public interface MetricCollector<T> {

    Metric<T> getMetric();

}
