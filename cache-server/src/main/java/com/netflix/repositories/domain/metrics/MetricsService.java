package com.netflix.repositories.domain.metrics;

import com.netflix.repositories.common.MetricTuple;
import com.netflix.repositories.domain.metrics.repositories.RepositoryMetricCache;
import com.spotify.github.v3.repos.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MetricsService {

    @Autowired
    private RepositoryMetricCache cache;

    public List<MetricTuple> getForkMetrics(int numRepos) {
        return cache.getView(ViewType.FORKS, numRepos);
    }

    public List<Repository> getRepositories() {
        return cache.getMetric().getValue();
    }



}
