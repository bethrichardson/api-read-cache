package com.netflix.repositories.domain.metrics;

import com.netflix.repositories.common.RepositoryMetric;

import java.util.List;

public interface MetricsCollector {

    List<RepositoryMetric> getForkMetrics();

}
