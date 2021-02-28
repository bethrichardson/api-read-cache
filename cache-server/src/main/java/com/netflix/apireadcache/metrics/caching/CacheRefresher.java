package com.netflix.apireadcache.metrics.caching;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CacheRefresher implements Runnable {

    private final RefreshingCache<?> cache;
    private final ScheduledExecutorService executorService;
    private ScheduledFuture<?> runningHandler;

    /**
     * Refreshes the data in the cache at the frequency specified on the cache.
     * @param cache The cache to refresh
     * @param executorService The cache executor service shares a pool of threads to refresh cache data
     */
    public CacheRefresher(RefreshingCache<?> cache, ScheduledExecutorService executorService) {
        this.cache = cache;
        this.executorService = executorService;
    }

    @Override
    public void run() {
        log.info("Starting cache refresh process for {} data.", cache.getMetricType());
        cache.refreshData();
    }

    public void cancel() {
        log.info("Shutting down CacheRefresher.");
        runningHandler.cancel(true);
    }

    public void start() {
        runningHandler = executorService.scheduleAtFixedRate(this, cache.getRefreshFrequency().toNanos(),
                cache.getRefreshFrequency().toNanos(), TimeUnit.NANOSECONDS);
    }


}
