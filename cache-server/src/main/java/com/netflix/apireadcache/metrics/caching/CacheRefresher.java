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
