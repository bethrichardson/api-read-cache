package com.netflix.apireadcache.metrics;

/**
 * Responsible for collecting a given metric from the source
 * @param <T> The type of data
 */
public interface MetricCollector<T> {

    Metric<T> getMetric();

    /**
     * All views that are supported for the data collected.
     * @return the supported views
     */
    ViewType[] getSupportedViews();

    MetricType getMetricType();

}
