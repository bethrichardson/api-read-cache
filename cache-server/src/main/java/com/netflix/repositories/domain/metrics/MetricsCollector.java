package com.netflix.repositories.domain.metrics;

import com.spotify.github.v3.repos.Repository;

import java.util.List;

public interface MetricsCollector {

    List<Repository> getRepositories();

}
