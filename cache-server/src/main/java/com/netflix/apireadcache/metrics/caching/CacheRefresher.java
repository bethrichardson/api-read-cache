package com.netflix.apireadcache.metrics.caching;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CacheRefresher implements Runnable {

    private ReloadingCache cache;
    private ScheduledFuture<?> runningHandler;

    public CacheRefresher(ReloadingCache cache) {
        this.cache = cache;
    }

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    @Override
    public void run() {
        log.info("Updating all values in metrics cache.");
        cache.refreshData();
    }

    public void cancel() {
        log.info("Shutting down CacheRefresher.");
        runningHandler.cancel(true);
    }

    public void start() {
        runningHandler = scheduler.scheduleAtFixedRate(this, cache.getRefreshInterval().toNanos(),
                cache.getRefreshInterval().toNanos(), TimeUnit.NANOSECONDS);
    }


}
