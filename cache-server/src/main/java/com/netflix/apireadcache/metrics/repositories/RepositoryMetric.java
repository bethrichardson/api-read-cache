/**
 * Copyright 2021 Netflix, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.apireadcache.metrics.repositories;

import com.netflix.apireadcache.metrics.Metric;
import com.netflix.apireadcache.metrics.MetricTuple;
import com.netflix.apireadcache.metrics.ViewType;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.kohsuke.github.GHRepository;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public class RepositoryMetric implements Metric<List<GHRepository>> {

    private final List<GHRepository> value;

    @Override
    public Map<ViewType, List<MetricTuple>> getViews() {
        Map<ViewType, List<MetricTuple>> views = new HashMap<>();
        addView(views, ViewType.FORKS, repo -> RepositoryMetric.getMetrics(repo, RepositoryMetric::getNumberForks));
        addView(views, ViewType.LAST_UPDATED, repo -> RepositoryMetric.getMetrics(repo, RepositoryMetric::getLastUpdated));
        addView(views, ViewType.OPEN_ISSUES, repo -> RepositoryMetric.getMetrics(repo, RepositoryMetric::getOpenIssues));
        addView(views, ViewType.STARS, repo -> RepositoryMetric.getMetrics(repo, RepositoryMetric::getNumberStars));
        return views;
    }

    private void addView(Map<ViewType, List<MetricTuple>> views, ViewType type, Function<List<GHRepository>, List<MetricTuple>> metricAggregationFunction) {
        views.put(type, metricAggregationFunction.apply(value));
    }

    @Override
    public List<GHRepository> getValue() {
        return value;
    }


    private static MetricTuple getMetrics(GHRepository repository, Function<GHRepository, Comparable> metricCollectionFunction) {
        if (metricCollectionFunction.apply(repository) != null) {
            return MetricTuple.builder()
                    .name(repository.getFullName())
                    .count(metricCollectionFunction.apply(repository))
                    .build();
        }
        return MetricTuple.emptyResult();
    }

    private static List<MetricTuple> getMetrics(List<GHRepository> repositories, Function<GHRepository, Comparable> metricCollectionFunction) {
        return repositories.stream()
                .map(it -> getMetrics(it, metricCollectionFunction))
                .filter(MetricTuple::isValid)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public static Integer getNumberForks(GHRepository repository) {
        return repository.getForksCount();
    }

    @SneakyThrows
    public static Comparable<Instant> getLastUpdated(GHRepository repository) {
        if (repository.getUpdatedAt() != null) {
            return repository.getUpdatedAt().toInstant();
        }
        return null;
    }

    public static Integer getOpenIssues(GHRepository repository) {
        return repository.getOpenIssueCount();
    }

    public static Integer getNumberStars(GHRepository repository) {
        return repository.getStargazersCount();
    }

}
