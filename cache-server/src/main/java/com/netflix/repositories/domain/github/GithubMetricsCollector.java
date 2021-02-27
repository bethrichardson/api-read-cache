package com.netflix.repositories.domain.github;

import com.netflix.repositories.common.RepositoryMetric;
import com.netflix.repositories.domain.metrics.Metrics;
import com.netflix.repositories.domain.metrics.MetricsCollector;
import com.spotify.github.v3.clients.RepositoryClient;
import com.spotify.github.v3.repos.Repository;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
public class GithubMetricsCollector implements MetricsCollector {

    private RepositoryClient client;

    public GithubMetricsCollector(RepositoryClient client) {
        this.client = client;
    }

    @Override
    public List<RepositoryMetric> getForkMetrics() {
        try {
            log.info("Retrieving fork metrics from GitHub.");
            CompletableFuture<List<Repository>> repoFuture = client.listOrganizationRepositories();
            if (repoFuture != null) {
                List<Repository> repositories = repoFuture.get();
                return Metrics.ofForks(repositories);
            } else {
                return Collections.emptyList();
            }
        } catch (InterruptedException | ExecutionException e) {
            log.warn("Failed to retrieve repository metrics.", e);
            return Collections.emptyList();
        }
    }

}
