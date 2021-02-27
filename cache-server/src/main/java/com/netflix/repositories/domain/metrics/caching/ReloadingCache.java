package com.netflix.repositories.domain.metrics.caching;

import com.netflix.repositories.common.RepositoryMetric;
import com.netflix.repositories.domain.metrics.ViewType;
import com.netflix.repositories.domain.metrics.MetricType;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ReloadingCache {

    public static final Duration DEFAULT_REFRESH_INTERVAL = Duration.ofMinutes(5); // TODO: make configurable
    private Duration refreshInterval;
    protected CacheRefresher cacheRefresher;
    ConcurrentHashMap<MetricType, Object> metricsCache;
    ConcurrentHashMap<ViewType, List<RepositoryMetric>> viewCache;

    public ReloadingCache() {
        setRefreshInterval(DEFAULT_REFRESH_INTERVAL);
    }

    void initializeCache() {
        metricsCache = new ConcurrentHashMap<>();
        viewCache = new ConcurrentHashMap<>();
        updateAllValues();
        cacheRefresher = new CacheRefresher(this);
        cacheRefresher.start();
    }

    protected void setRefreshInterval(Duration refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    Duration getRefreshInterval() {
        return refreshInterval;
    }

    abstract void updateAllValues();

}
