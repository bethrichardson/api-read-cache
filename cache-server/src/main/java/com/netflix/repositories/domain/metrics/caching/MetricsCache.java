package com.netflix.repositories.domain.metrics.caching;

import com.netflix.repositories.common.RepositoryMetric;
import com.netflix.repositories.domain.metrics.MetricType;
import com.netflix.repositories.domain.metrics.Metrics;
import com.netflix.repositories.domain.metrics.MetricsCollector;
import com.netflix.repositories.domain.metrics.ViewType;
import com.spotify.github.v3.repos.Repository;
import lombok.SneakyThrows;

import java.util.List;

import static com.netflix.repositories.domain.metrics.MetricType.REPOSITORIES;
import static com.netflix.repositories.domain.metrics.ViewType.FORKS;

public class MetricsCache extends ReloadingCache {

    private final MetricsCollector metricsCollector;

    public MetricsCache(MetricsCollector metricsCollector) {
        super();
        this.metricsCollector = metricsCollector;
    }

    @SneakyThrows
    private void cacheView(ViewType type, List<RepositoryMetric> metrics) {
        viewCache.put(type, metrics);
    }

    @SneakyThrows
    private void cacheMetric(MetricType type, Object response) {
        metricsCache.put(type, response);
    }

    @SneakyThrows
    public List<RepositoryMetric> getView(ViewType viewType, int numResults) {
        List<RepositoryMetric> metrics = viewCache.get(viewType);
        if (metrics.size() > numResults) {
            metrics = metrics.subList(0, numResults);
        }
        return metrics;
    }

    public Object getMetric(MetricType metricType) {
        return metricsCache.get(metricType);
    }

    @Override
    void updateAllValues() {
        List<Repository> repositories = metricsCollector.getRepositories();
        List<RepositoryMetric> forkMetrics = Metrics.ofForks(repositories);
        cacheMetric(REPOSITORIES, repositories);
        cacheView(FORKS, forkMetrics);
    }
}
