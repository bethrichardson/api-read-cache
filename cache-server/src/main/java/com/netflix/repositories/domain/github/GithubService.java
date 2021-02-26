package com.netflix.repositories.domain.github;

import com.netflix.repositories.common.RepositoryMetric;
import com.netflix.repositories.domain.Metrics;
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

    public List<RepositoryMetric> getRepositoryMetrics(int numRepositories) {
        try {
            List<Repository> repositories = client.listOrganizationRepositories().get().subList(0, numRepositories);
            return Metrics.ofForks(repositories);
        } catch (InterruptedException | ExecutionException e) {
            log.warn("Failed to retrieve repository metrics.", e);
            return Collections.emptyList();
        }
    }

}
