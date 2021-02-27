package com.netflix.repositories.domain.metrics;

import com.netflix.repositories.common.MetricTuple;
import com.netflix.repositories.domain.metrics.members.MembersMetricCache;
import com.netflix.repositories.domain.metrics.organization.OrganizationMetricCache;
import com.netflix.repositories.domain.metrics.overview.OverviewMetricCache;
import com.netflix.repositories.domain.metrics.repositories.RepositoryMetricCache;
import com.spotify.github.v3.repos.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MetricsService {

    @Autowired
    private RepositoryMetricCache repositoryCache;

    @Autowired
    private MembersMetricCache membersCache;

    @Autowired
    private OrganizationMetricCache organizationCache;

    @Autowired
    private OverviewMetricCache overviewCache;

    public Object getOverview() {
        return overviewCache.getMetric().getValue();
    }

    public Object getOrganization() {
        return organizationCache.getMetric().getValue();
    }

    public Object getMembers() {
        return membersCache.getMetric().getValue();
    }

    public List<Repository> getRepositories() {
        return repositoryCache.getMetric().getValue();
    }

    public List<MetricTuple> getForkMetrics(int numRepos) {
        return repositoryCache.getView(ViewType.FORKS, numRepos);
    }

}
