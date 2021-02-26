package com.netflix.repositories.domain.github;

import com.netflix.repositories.common.RepositoryMetric;
import com.netflix.repositories.domain.metrics.Metrics;
import com.spotify.github.v3.clients.RepositoryClient;
import com.spotify.github.v3.repos.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("PMD.BeanMembersShouldSerialize") // TODO: ideally this would be disabled for this project
@Component
@Slf4j
public class GithubService {

    @Autowired
    private RepositoryClient client;

    public List<RepositoryMetric> getForkMetrics(int numRepositories) {
        return getForkMetrics().subList(0, numRepositories);
    }

    public List<RepositoryMetric> getForkMetrics() {
        try {
            List<Repository> repositories = client.listOrganizationRepositories().get();
            return Metrics.ofForks(repositories);
        } catch (InterruptedException | ExecutionException e) {
            log.warn("Failed to retrieve repository metrics.", e);
            return Collections.emptyList();
        }
    }

}
