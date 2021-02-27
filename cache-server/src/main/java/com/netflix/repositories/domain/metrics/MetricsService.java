package com.netflix.repositories.domain.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.repositories.common.MetricTuple;
import com.netflix.repositories.domain.metrics.github.CachingGitHubClient;
import com.netflix.repositories.domain.metrics.proxied.ProxiedMetricCache;
import com.netflix.repositories.domain.metrics.repositories.RepositoryMetricCache;
import com.spotify.github.v3.repos.Repository;
import lombok.SneakyThrows;
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
    CachingGitHubClient gitHubClient;

    @Autowired
    private RepositoryMetricCache repositoryCache;

    @Autowired
    private ObjectMapper githubObjectMapper;

    public Object getOverview() {
        return overviewMetricCache.getMetric().getValue();
    }

    public Object getOrganization() {
        return organizationMetricCache.getMetric().getValue();
    }

    public Object getMembers() {
        return membersMetricCache.getMetric().getValue();
    }

    public Object getProxiedResponse(String path) {
        return gitHubClient.getUnhandledRoute(path);
    }

    public List<Repository> getRepositories() {
        return repositoryCache.getMetric().getValue();
    }

    @SneakyThrows
    public String getRepositoriesAsJsonString() {
        return githubObjectMapper.writeValueAsString(getRepositories());
    }

    public List<MetricTuple> getMetricsByForkCount(int numRepos) {
        return repositoryCache.getView(ViewType.FORKS, numRepos);
    }

    public List<MetricTuple> getMetricsByLastUpdated(int numRepos) {
        return repositoryCache.getView(ViewType.LAST_UPDATED, numRepos);
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
