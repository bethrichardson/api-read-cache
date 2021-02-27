package com.netflix.repositories.domain.metrics.caching;

import com.netflix.repositories.common.MetricTuple;
import com.netflix.repositories.domain.metrics.Metric;
import com.netflix.repositories.domain.metrics.MetricCollector;
import com.netflix.repositories.domain.metrics.ViewType;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MetricsCache<T> extends ReloadingCache<T> {

    private final MetricCollector<T> metricCollector;

    public MetricsCache(MetricCollector<T> metricCollector) {
        super();
        this.metricCollector = metricCollector;
    }

    protected void cacheMetric(Metric<T> response) {
        metricsCache.set(response);
    }

    public Metric<T> getMetric() {
        return metricsCache.get();
    }

    private void cacheView(ViewType type, List<MetricTuple> metricTuples) {
        viewCache.put(type, metricTuples);
    }

    public List<MetricTuple> getView(ViewType viewType, int numResults) {
        List<MetricTuple> metricTuples = viewCache.get(viewType);
        if (metricTuples == null) {
            return Collections.emptyList();
        }
        if (metricTuples.size() > numResults) {
            metricTuples = metricTuples.subList(0, numResults);
        }
        return metricTuples;
    }

    @Override
    public void refreshData() {
        Metric<T> metric = metricCollector.getMetric();
        if (metric != null) { // prefer to retain data
            updateAllViews(metric);
            cacheMetric(metric);
        }
    }

    protected void updateAllViews(Metric<T> metric) {
        Map<ViewType, List<MetricTuple>> views = metric.getViews();
        views.forEach(this::cacheView);
    }
}
