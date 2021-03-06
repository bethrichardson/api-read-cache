/**
 * Copyright 2021 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.apireadcache.metrics;

import com.netflix.apireadcache.metrics.github.ProxiedGitHubClient;
import com.netflix.apireadcache.metrics.proxied.ProxiedMetricCache;
import com.netflix.apireadcache.metrics.repositories.RepositoryMetricCache;
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
    private ProxiedGitHubClient gitHubClient;

    @Autowired
    private RepositoryMetricCache repositoryMetricCache;

    @Autowired
    private ProxiedMetricCache repositoryViewCache;

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

    public Object getRepositories() {
        return repositoryViewCache.getMetric().getValue();
    }
    public List<MetricTuple> getTopMetricsByForkCount(int numRepos) {
        return repositoryMetricCache.getView(ViewType.FORKS, numRepos);
    }

    public List<MetricTuple> getTopMetricsByLastUpdated(int numRepos) {
        return repositoryMetricCache.getView(ViewType.LAST_UPDATED, numRepos);
    }

    public List<MetricTuple> getMetricsByOpenIssues(int numRepos) {
        return repositoryMetricCache.getView(ViewType.OPEN_ISSUES, numRepos);
    }

    public List<MetricTuple> getTopMetricsByStars(int numRepos) {
        return repositoryMetricCache.getView(ViewType.STARS, numRepos);
    }

    /**
     * This could be exposed to clear the cache via service call with
     * appropriate access control.
     */
    public void refreshAllData() {
        overviewMetricCache.refreshData();
        organizationMetricCache.refreshData();
        membersMetricCache.refreshData();
        repositoryMetricCache.refreshData();
        repositoryViewCache.refreshData();
    }

}
