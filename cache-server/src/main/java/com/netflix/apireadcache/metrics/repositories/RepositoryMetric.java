/**
 * Copyright 2021 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
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
import com.spotify.github.v3.repos.Repository;
import lombok.AllArgsConstructor;

import java.time.Instant;
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


    private static MetricTuple getMetrics(Repository repository, Function<Repository, Comparable> metricCollectionFunction) {
        if (metricCollectionFunction.apply(repository) != null) {
            return MetricTuple.builder()
                    .name(repository.fullName())
                    .count(metricCollectionFunction.apply(repository))
                    .build();
        }
        return MetricTuple.emptyResult();
    }

    private static List<MetricTuple> getMetrics(List<Repository> repositories, Function<Repository, Comparable> metricCollectionFunction) {
        return repositories.stream()
                .map(it -> getMetrics(it, metricCollectionFunction))
                .filter(MetricTuple::isValid)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    public static Integer getNumberForks(Repository repository) {
        if (repository.forksCount()!= null) {
            return repository.forksCount();
        }
        return null;
    }

    public static Comparable<Instant> getLastUpdated(Repository repository) {
        if (repository.updatedAt() != null && repository.updatedAt().instant() != null) {
            return repository.updatedAt().instant();
        }
        return null;
    }

    public static Integer getOpenIssues(Repository repository) {
        if (repository.openIssuesCount()!= null) {
            return repository.openIssuesCount();
        }
        return null;
    }

    public static Integer getNumberStars(Repository repository) {
        if (repository.stargazersCount()!= null) {
            return repository.stargazersCount();
        }
        return null;
    }

}
