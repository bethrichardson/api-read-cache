package com.netflix.repositories.domain.metrics;

import com.netflix.repositories.common.MetricTuple;
import com.netflix.repositories.domain.metrics.proxied.ProxiedMetricCache;
import com.netflix.repositories.domain.metrics.repositories.RepositoryMetricCache;
import com.spotify.github.v3.repos.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MetricsService {

    @Autowired
    private ProxiedMetricCache overviewMetricCache;

    @Autowired
    private ProxiedMetricCache organizationMetricCache;

    @Autowired
    private ProxiedMetricCache membersMetricCache;

    @Autowired
    private RepositoryMetricCache repositoryCache;

    public Object getOverview() {
        return overviewMetricCache.getMetric().getValue();
    }

    public Object getOrganization() {
        return organizationMetricCache.getMetric().getValue();
    }

    public Object getMembers() {
        return membersMetricCache.getMetric().getValue();
    }

    public List<Repository> getRepositories() {
        return repositoryCache.getMetric().getValue();
    }

    public List<MetricTuple> getForkMetrics(int numRepos) {
        return repositoryCache.getView(ViewType.FORKS, numRepos);
    }

    /**
     * This could be exposed to clear the cache via service call with
     * appropriate access control.
     */
    public void refreshAllData() {
        overviewMetricCache.refreshData();
        organizationMetricCache.refreshData();
        membersMetricCache.refreshData();
        repositoryCache.refreshData();
    }

}
