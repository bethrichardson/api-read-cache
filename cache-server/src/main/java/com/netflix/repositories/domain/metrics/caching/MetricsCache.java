package com.netflix.repositories.domain.metrics.caching;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.netflix.repositories.common.RepositoryMetric;
import com.netflix.repositories.domain.metrics.MetricType;
import com.netflix.repositories.domain.metrics.MetricsCollector;
import lombok.SneakyThrows;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetricsCache {

    public static final Duration DEFAULT_REFRESH_INTERVAL = Duration.ofMinutes(5); // TODO: make configurable
    private static final String THREAD_PREFIX = "metrics-cache";
    private Duration refreshInterval;

    private final MetricsCollector metricsCollector;
    private final TimeProvider timeProvider;
    protected CacheRefresher cacheRefresher;

    private LoadingCache<String, List<RepositoryMetric>> cache;

    public MetricsCache(MetricsCollector metricsCollector, TimeProvider timeProvider) {
        this.metricsCollector = metricsCollector;
        this.timeProvider = timeProvider;
        setRefreshInterval(DEFAULT_REFRESH_INTERVAL);
    }

    void initializeCache() {
        cache = CacheBuilder.newBuilder()
                .refreshAfterWrite(refreshInterval)
                .ticker(timeProvider)
                .build(CacheLoader.asyncReloading(new MetricsCacheLoader(), ThreadPoolExecutorFactory.build(THREAD_PREFIX)));
        updateAllValues();
        cacheRefresher = new CacheRefresher(this);
        cacheRefresher.start();
    }

    private class MetricsCacheLoader extends CacheLoader<String, List<RepositoryMetric>> {
        @Override
        public List<RepositoryMetric> load(String metricName) {
            return updateMetricsInCache(MetricType.valueOf(metricName));
        }
    }

    private void setRefreshInterval(Duration refreshInterval) {
        this.refreshInterval = refreshInterval;
    }

    Duration getRefreshInterval() {
        return refreshInterval;
    }

    protected void updateAllValues() {
        cache.invalidateAll();
        Arrays.stream(MetricType.values()).map(MetricType::name).forEach(cache::getUnchecked);
    }

    private List<RepositoryMetric> updateMetricsInCache(MetricType metricType) {
        switch (metricType) {
            case FORKS:
                return metricsCollector.getForkMetrics();
            default:
                return new ArrayList<>();

        }
    }

    @SneakyThrows
    public List<RepositoryMetric> getMetric(MetricType metricType) {
        try {
            return cache.getUnchecked(metricType.name());
        } catch (UncheckedExecutionException e) {
            throw e.getCause();
        }
    }

}
