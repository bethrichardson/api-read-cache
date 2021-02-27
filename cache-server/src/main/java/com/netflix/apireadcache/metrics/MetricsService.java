package com.netflix.apireadcache.metrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.apireadcache.metrics.github.ProxiedGitHubClient;
import com.netflix.apireadcache.metrics.proxied.ProxiedMetricCache;
import com.netflix.apireadcache.metrics.repositories.RepositoryMetricCache;
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
    ProxiedGitHubClient gitHubClient;

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

    public List<MetricTuple> getTopMetricsByForkCount(int numRepos) {
        return repositoryCache.getView(ViewType.FORKS, numRepos);
    }

    public List<MetricTuple> getTopMetricsByLastUpdated(int numRepos) {
        return repositoryCache.getView(ViewType.LAST_UPDATED, numRepos);
    }

    public List<MetricTuple> getMetricsByOpenIssues(int numRepos) {
        return repositoryCache.getView(ViewType.OPEN_ISSUES, numRepos);
    }

    public List<MetricTuple> getTopMetricsByStars(int numRepos) {
        return repositoryCache.getView(ViewType.STARS, numRepos);
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
