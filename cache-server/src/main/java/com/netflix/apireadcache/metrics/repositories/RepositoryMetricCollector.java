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
package com.netflix.apireadcache.metrics.repositories;

import com.netflix.apireadcache.metrics.MetricCollector;
import com.netflix.apireadcache.metrics.MetricType;
import com.netflix.apireadcache.metrics.ViewType;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Collects metrics on GitHub repositories for an organization.
 * Also provides a set of views for that data
 */
@Slf4j
public class RepositoryMetricCollector implements MetricCollector<List<GHRepository>> {

    private final GitHub client;
    String organization;

    public RepositoryMetricCollector(GitHub client, String organization) {
        this.client = client;
        this.organization = organization;
    }

    @Override
    public RepositoryMetric getMetric() {
        try {
            log.info("Retrieving repository data from GitHub.");
            GHOrganization githubOrganization = client.getOrganization(organization);
            if (githubOrganization != null) { // This should only be null during initialization when using mock clients
                List<GHRepository> repositories = githubOrganization.listRepositories().toList();
                return new RepositoryMetric(repositories);
            }
        } catch (IOException e) {
            log.warn("Failed to retrieve repositories.", e);
        }
        return new RepositoryMetric(Collections.emptyList());
    }

    @Override
    public ViewType[] getSupportedViews() {
        return ViewType.values();
    }

    @Override
    public MetricType getMetricType() {
        return MetricType.REPOSITORY_METRICS;
    }

}
