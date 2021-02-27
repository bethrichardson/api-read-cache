package com.netflix.apireadcache.metrics.repositories;

import com.netflix.apireadcache.metrics.Metric;
import com.netflix.apireadcache.metrics.MetricTuple;
import com.netflix.apireadcache.metrics.ViewType;
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
        addView(views, ViewType.FORKS, repo -> RepositoryMetric.getMetrics(repo, RepositoryMetric::getNumberForks));
        addView(views, ViewType.LAST_UPDATED, repo -> RepositoryMetric.getMetrics(repo, RepositoryMetric::getLastUpdated));
        addView(views, ViewType.OPEN_ISSUES, repo -> RepositoryMetric.getMetrics(repo, RepositoryMetric::getOpenIssues));
        addView(views, ViewType.STARS, repo -> RepositoryMetric.getMetrics(repo, RepositoryMetric::getNumberStars));
        return views;
    }

    private void addView(Map<ViewType, List<MetricTuple>> views, ViewType type, Function<List<Repository>, List<MetricTuple>> metricAggregationFunction) {
        views.put(type, metricAggregationFunction.apply(value));
    }

    @Override
    public List<Repository> getValue() {
        return value;
    }


    private static MetricTuple getMetrics(Repository repository, Function<Repository, Long> metricCollectionFunction) {
        if (metricCollectionFunction.apply(repository) != null) {
            return MetricTuple.builder()
                    .name(repository.name())
                    .count(metricCollectionFunction.apply(repository))
                    .build();
        }
        return MetricTuple.emptyResult();
    }

    private static List<MetricTuple> getMetrics(List<Repository> repositories, Function<Repository, Long> metricCollectionFunction) {
        return repositories.stream()
                .map(it -> getMetrics(it, metricCollectionFunction))
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public static Long getNumberForks(Repository repository) {
        if (repository.forksCount()!= null) {
            return Long.valueOf(repository.forksCount());
        }
        return null;
    }

    public static Long getLastUpdated(Repository repository) {
        if (repository.updatedAt() != null && repository.updatedAt().instant() != null) {
            return repository.updatedAt().instant().getEpochSecond();
        }
        return null;
    }

    public static Long getOpenIssues(Repository repository) {
        if (repository.openIssuesCount()!= null) {
            return Long.valueOf(repository.openIssuesCount());
        }
        return null;
    }

    public static Long getNumberStars(Repository repository) {
        if (repository.stargazersCount()!= null) {
            return Long.valueOf(repository.stargazersCount());
        }
        return null;
    }

}
