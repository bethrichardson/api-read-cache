package com.netflix.apireadcache.metrics.repositories;

import com.netflix.apireadcache.metrics.MetricCollector;
import com.spotify.github.v3.clients.RepositoryClient;
import com.spotify.github.v3.repos.Repository;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
            if (repoFuture != null) {
                return new RepositoryMetric(repoFuture.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.warn("Failed to retrieve repositories.", e);
        }
        return new RepositoryMetric(Collections.emptyList());
    }

}
