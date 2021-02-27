package com.netflix.repositories.domain.metrics.overview;

import com.netflix.repositories.domain.metrics.github.CachingGitHubClient;
import com.netflix.repositories.domain.metrics.proxied.ProxiedMetric;
import com.netflix.repositories.domain.metrics.proxied.ProxiedMetricCollector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OverviewMetricCollector extends ProxiedMetricCollector {

    CachingGitHubClient gitHubClient;

    public OverviewMetricCollector(CachingGitHubClient client) {
        this.gitHubClient = client;
    }

    @Override
    public ProxiedMetric getMetric() {
        return new ProxiedMetric(gitHubClient.getOverview());
    }
}
