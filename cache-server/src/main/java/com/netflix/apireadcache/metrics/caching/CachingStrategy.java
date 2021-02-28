package com.netflix.apireadcache.metrics.caching;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;

@Builder
@Data
public class CachingStrategy {

    /**
     * New metrics are collected at this frequency
     */
    private Duration refreshFrequency;

    /**
     * The number of threads to use for cache refresh
     */
    private ScheduledExecutorService executorService;

}
