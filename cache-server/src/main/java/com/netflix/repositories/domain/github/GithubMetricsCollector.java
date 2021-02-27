package com.netflix.repositories.domain.github;

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
    public List<Repository> getRepositories() {
        try {
            log.info("Retrieving repository data from GitHub.");
            CompletableFuture<List<Repository>> repoFuture = client.listOrganizationRepositories();
            if (repoFuture != null) {
                return repoFuture.get();
            } else {
                return Collections.emptyList();
            }
        } catch (InterruptedException | ExecutionException e) {
            log.warn("Failed to retrieve repositories.", e);
            return Collections.emptyList();
        }
    }

}
