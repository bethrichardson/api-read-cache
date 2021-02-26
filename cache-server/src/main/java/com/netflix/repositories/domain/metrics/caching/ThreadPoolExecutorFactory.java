package com.netflix.repositories.domain.metrics.caching;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class ThreadPoolExecutorFactory {

    public static ThreadPoolTaskExecutor build(String threadNamePrefix) {
        if (threadNamePrefix == null) {
            throw new IllegalArgumentException();
        }

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(1);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.initialize();
        return executor;
    }
}
