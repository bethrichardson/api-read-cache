package com.netflix.repositories.domain.metrics.caching;

import com.netflix.repositories.common.MetricTuple;
import com.netflix.repositories.domain.metrics.Metric;
import com.netflix.repositories.domain.metrics.ViewType;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ReloadingCache<T> {

    public static final Duration DEFAULT_REFRESH_INTERVAL = Duration.ofMinutes(5); // TODO: make configurable
    private Duration refreshInterval;
    protected CacheRefresher cacheRefresher;
    AtomicReference<Metric<T>> metricsCache;
    ConcurrentHashMap<ViewType, List<MetricTuple>> viewCache;

    public ReloadingCache() {
        setRefreshInterval(DEFAULT_REFRESH_INTERVAL);
    }

    public void initializeCache(ViewType[] viewTypes) {
        metricsCache = new AtomicReference<>();
        initializeViews(viewTypes);
        refreshData();
        cacheRefresher = new CacheRefresher(this);
        cacheRefresher.start();
    }

    private void initializeViews(ViewType[] viewTypes) {
        viewCache = new ConcurrentHashMap<>();
        Arrays.asList(viewTypes).forEach(type -> viewCache.put(type, new ArrayList<>()));
    }

    protected void setRefreshInterval(Duration refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    Duration getRefreshInterval() {
        return refreshInterval;
    }

    abstract void refreshData();

}
