package com.netflix.apireadcache.metrics.repositories;

import com.netflix.apireadcache.metrics.MetricCollector;
import com.netflix.apireadcache.metrics.MetricType;
import com.netflix.apireadcache.metrics.ViewType;
import com.spotify.github.v3.clients.RepositoryClient;
import com.spotify.github.v3.repos.Repository;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Collects metrics on GitHub repositories for an organization.
 * Also provides a set of views for that data
 */
@Slf4j
public class RepositoryMetricCollector implements MetricCollector<List<Repository>> {

    private final RepositoryClient client;

    public RepositoryMetricCollector(RepositoryClient client) {
        this.client = client;
    }

    @Override
    public RepositoryMetric getMetric() {
        try {
            log.info("Retrieving repository data from GitHub.");
            CompletableFuture<List<Repository>> repoFuture = client.listOrganizationRepositories();
            if (repoFuture != null) { // This should only be null during initialization when using mock clients
                return new RepositoryMetric(repoFuture.get());
            }
        } catch (InterruptedException | ExecutionException e) {
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
