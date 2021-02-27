package com.netflix.repositories.domain.metrics.members;

import com.netflix.repositories.domain.metrics.github.CachingGitHubClient;
import com.netflix.repositories.domain.metrics.proxied.ProxiedMetric;
import com.netflix.repositories.domain.metrics.proxied.ProxiedMetricCollector;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MembersMetricCollector extends ProxiedMetricCollector {

    String organizationName;
    CachingGitHubClient gitHubClient;

    public MembersMetricCollector(CachingGitHubClient client, String organizationName) {
        this.gitHubClient = client;
        this.organizationName = organizationName;
    }

    @Override
    public ProxiedMetric getMetric() {
        return new ProxiedMetric(gitHubClient.getOrganizationMembers(organizationName));
    }
}
