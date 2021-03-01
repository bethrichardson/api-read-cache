/**
 * Copyright 2021 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.apireadcache.metrics.caching;

import com.netflix.apireadcache.metrics.Metric;
import com.netflix.apireadcache.metrics.MetricCollector;
import com.netflix.apireadcache.metrics.MetricTuple;
import com.netflix.apireadcache.metrics.ViewType;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Stores a reference to a given type of data and refreshes it from the metricCollector according to the configured refreshFrequency.
 * The cache refreshes all views for the metric at the time that it refreshes data.
 * @param <T> The type of data stored in the cache
 */
 @Slf4j
public class MetricsCache<T> extends RefreshingCache<T> {

    private final MetricCollector<T> metricCollector;

    /**
     * @param metricCollector The collection source for the metric cached
     * @param strategy The strategy for how to refresh the data in the cache
     */
    public MetricsCache(MetricCollector<T> metricCollector, CachingStrategy strategy) {
        super(metricCollector.getMetricType(), metricCollector.getSupportedViews(), strategy);
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
        log.info("Refreshing data for {}", metricCollector.getMetricType());
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
