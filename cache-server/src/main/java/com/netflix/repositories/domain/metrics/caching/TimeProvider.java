package com.netflix.repositories.domain.metrics.caching;

public class TimeProvider {

    public long getNanoTime() {
        return System.nanoTime();
    }

}
