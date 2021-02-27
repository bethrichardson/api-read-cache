package com.netflix.repositories.domain.metrics.caching;

import com.google.common.base.Ticker;

public class TimeProvider extends Ticker {

    public long getNanoTime() {
        return System.nanoTime();
    }

    @Override
    public long read() {
        return getNanoTime();
    }
}
