package com.netflix.repositories.domain.metrics.caching;

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.netflix.repositories.common.RepositoryMetric;
import com.netflix.repositories.domain.github.GithubService;
import com.netflix.repositories.domain.metrics.MetricType;
import lombok.SneakyThrows;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetricsCache {

    private static final Duration REFRESH_INTERVAL = Duration.ofMinutes(5); // TODO: make configurable
    private static final String THREAD_PREFIX = "metrics-cache";

    private GithubService githubService;
    private final TimeProvider timeProvider;

    private LoadingCache<String, List<RepositoryMetric>> metricsCache;

    public MetricsCache(GithubService service, TimeProvider timeProvider) {
        this.githubService = service;
        this.timeProvider = timeProvider;
    }

    void initializeCache() {
        metricsCache = CacheBuilder.newBuilder()
                .refreshAfterWrite(REFRESH_INTERVAL)
                .ticker(new Ticker() {
                    @Override
                    public long read() {
                        return timeProvider.getNanoTime();
                    }
                })
                .build(CacheLoader.asyncReloading(new CacheLoader<String, List<RepositoryMetric>>() {
                    @Override
                    public List<RepositoryMetric> load(String metricName) {
                        return updateMetricsInCache(MetricType.valueOf(metricName));
                    }
                }, ThreadPoolExecutorFactory.build(THREAD_PREFIX)));
        Arrays.stream(MetricType.values()).forEach(this::getMetric);
    }

    private List<RepositoryMetric> updateMetricsInCache(MetricType metricType) {
        switch (metricType) {
            case FORKS:
                return githubService.getForkMetrics();
            default:
                return new ArrayList<>();

        }
    }

    @SneakyThrows
    public List<RepositoryMetric> getMetric(MetricType metricType) {
        try {
            return metricsCache.getUnchecked(metricType.name());
        } catch (UncheckedExecutionException e) {
            throw e.getCause();
        }
    }




}
