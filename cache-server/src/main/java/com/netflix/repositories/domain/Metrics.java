package com.netflix.repositories.domain;

import com.netflix.repositories.common.RepositoryMetric;
import com.spotify.github.v3.repos.Repository;

import java.util.List;
import java.util.stream.Collectors;

public class Metrics {

    public static RepositoryMetric ofForks(Repository repository) {
        return RepositoryMetric.builder()
                .name(repository.name())
                .count(repository.forksCount())
                .build();
    }

    public static List<RepositoryMetric> ofForks(List<Repository> repositories) {
        return repositories.stream()
                .map(Metrics::ofForks)
                .collect(Collectors.toList());
    }
}
