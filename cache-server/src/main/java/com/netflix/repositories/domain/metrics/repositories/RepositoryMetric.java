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
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RepositoryMetric implements Metric<List<Repository>> {

    private final List<Repository> value;

    @Override
    public Map<ViewType, List<MetricTuple>> getViews() {
        Map<ViewType, List<MetricTuple>> views = new HashMap<>();
        addView(views, ViewType.FORKS, RepositoryMetric::getForkMetrics);
        addView(views, ViewType.LAST_UPDATED, RepositoryMetric::getLastUpdatedMetrics);
        addView(views, ViewType.OPEN_ISSUES, RepositoryMetric::getOpenIssueMetrics);
        addView(views, ViewType.STARS, RepositoryMetric::getStarMetrics);
        return views;
    }

    private void addView(Map<ViewType, List<MetricTuple>> views, ViewType type, Function<List<Repository>, List<MetricTuple>> metricAggregationFunction) {
        views.put(type, metricAggregationFunction.apply(value));
    }

    @Override
    public List<Repository> getValue() {
        return value;
    }

    public static MetricTuple getForkMetrics(Repository repository) {
        if (repository.forksCount() != null) {
            return MetricTuple.builder()
                    .name(repository.name())
                    .count(Long.valueOf(repository.forksCount()))
                    .build();
        }
        return MetricTuple.emptyResult();
    }

    public static List<MetricTuple> getForkMetrics(List<Repository> repositories) {
        return repositories.stream()
                .map(RepositoryMetric::getForkMetrics)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public static MetricTuple getLastUpdatedMetrics(Repository repository) {
        if (repository.updatedAt() != null && repository.updatedAt().instant() != null) {
            return MetricTuple.builder()
                    .name(repository.name())
                    .count(repository.updatedAt().instant().getEpochSecond())
                    .build();
        }
        return MetricTuple.emptyResult();
    }

    public static List<MetricTuple> getLastUpdatedMetrics(List<Repository> repositories) {
        return repositories.stream()
                .map(RepositoryMetric::getLastUpdatedMetrics)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public static List<MetricTuple> getOpenIssueMetrics(List<Repository> repositories) {
        return repositories.stream()
                .map(RepositoryMetric::getOpenIssueMetrics)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public static MetricTuple getOpenIssueMetrics(Repository repository) {
        if (repository.openIssuesCount() != null) {
            return MetricTuple.builder()
                    .name(repository.name())
                    .count(Long.valueOf(repository.openIssuesCount()))
                    .build();
        }
        return MetricTuple.emptyResult();
    }

    public static List<MetricTuple> getStarMetrics(List<Repository> repositories) {
        return repositories.stream()
                .map(RepositoryMetric::getOpenIssueMetrics)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public static MetricTuple getStarMetrics(Repository repository) {
        if (repository.stargazersCount() != null) {
            return MetricTuple.builder()
                    .name(repository.name())
                    .count(Long.valueOf(repository.stargazersCount()))
                    .build();
        }
        return MetricTuple.emptyResult();
    }

}
