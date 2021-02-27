package com.netflix.repositories.domain.metrics.repositories;

import com.netflix.repositories.common.MetricTuple;
import com.netflix.repositories.domain.metrics.Metric;
import com.netflix.repositories.domain.metrics.ViewType;
import com.spotify.github.v3.repos.Repository;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RepositoryMetric implements Metric<List<Repository>> {

    private final List<Repository> value;

    @Override
    public Map<ViewType, List<MetricTuple>> getViews() {
        Map<ViewType, List<MetricTuple>> views = new HashMap<>();
        List<MetricTuple> forkMetrics = ofForks(value);
        views.put(ViewType.FORKS, forkMetrics);
        return views;
    }

    @Override
    public List<Repository> getValue() {
        return value;
    }

    public static MetricTuple ofForks(Repository repository) {
        return MetricTuple.builder()
                .name(repository.name())
                .count(repository.forksCount())
                .build();
    }

    public static List<MetricTuple> ofForks(List<Repository> repositories) {
        return repositories.stream()
                .map(RepositoryMetric::ofForks)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }
}
