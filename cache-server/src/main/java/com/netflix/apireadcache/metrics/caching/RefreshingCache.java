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
import com.netflix.apireadcache.metrics.MetricTuple;
import com.netflix.apireadcache.metrics.MetricType;
import com.netflix.apireadcache.metrics.ViewType;
import lombok.Getter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;

public abstract class RefreshingCache<T> {

    @Getter
    private final Duration refreshFrequency;
    @Getter
    private final MetricType metricType;
    private final ScheduledExecutorService executorService;
    protected CacheRefresher cacheRefresher;
    protected AtomicReference<Metric<T>> metricsCache;
    protected ConcurrentHashMap<ViewType, List<MetricTuple>> viewCache;

    public RefreshingCache(MetricType metricType, ViewType[] viewTypes, CachingStrategy strategy) {
        this.refreshFrequency = strategy.getRefreshFrequency();
        this.metricType = metricType;
        this.executorService = strategy.getExecutorService();
        metricsCache = new AtomicReference<>();
        initializeViews(viewTypes);
    }

    public void initializeCache() {
        refreshData();
        cacheRefresher = new CacheRefresher(this, executorService);
        cacheRefresher.start();
    }

    private void initializeViews(ViewType[] viewTypes) {
        viewCache = new ConcurrentHashMap<>();
        Arrays.asList(viewTypes).forEach(type -> viewCache.put(type, new ArrayList<>()));
    }

    abstract public void refreshData();

}
