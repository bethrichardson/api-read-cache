package com.netflix.repositories.domain.metrics;

public interface MetricCollector<T> {

    Metric<T> getMetric();

}
